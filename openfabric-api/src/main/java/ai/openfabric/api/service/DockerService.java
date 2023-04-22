package ai.openfabric.api.service;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.WorkerStats;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.repository.WorkerStatsRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.InvocationBuilder.AsyncResultCallback;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

@Service
@EnableScheduling
public class DockerService {

    private final DockerClient client;
    private final WorkerStatsRepository workerStatsRepository;
    private final WorkerRepository workerRepository;

    public DockerService(DockerClient client, WorkerRepository workerRepository, WorkerStatsRepository workerStatsRepository) {
        this.client = client;
        this.workerRepository = workerRepository;
        this.workerStatsRepository = workerStatsRepository;
    }

    public Container getContainer(String id) {
        List<Container> containers = client.listContainersCmd().withShowAll(true).withIdFilter(Collections.singletonList(id)).exec();
        if (containers.size() == 0) {
            return null;
        }
        return containers.get(0);
    }

    public List<Container> getAllContainers() {
        return client.listContainersCmd().withShowAll(true).exec();
    }


    public void startWorker(String id) throws NotModifiedException {
        client.startContainerCmd(id).exec();
    }

    public void stopWorker(String id) throws NotModifiedException {
        client.stopContainerCmd(id).exec();
    }

    @Scheduled(fixedDelay = 4000)
    public void workerStats() {
        System.out.println("workerStats");
        List<Container> containers = client.listContainersCmd().exec();
        for (Container container : containers) {
            Worker worker = workerRepository.findByDockerId(container.getId());
            AsyncResultCallback<Statistics> callback = new AsyncResultCallback<>();
            client.statsCmd(container.getId()).exec(callback);
            try {
                Statistics stats = callback.awaitResult();
                callback.close();
                WorkerStats workerStats = WorkerStats.builder().worker(worker).build();
                workerStats.setFromStatistics(stats);
                workerStatsRepository.save(workerStats);
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
