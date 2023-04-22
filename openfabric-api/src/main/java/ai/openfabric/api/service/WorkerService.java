package ai.openfabric.api.service;

import ai.openfabric.api.exception.WorkerStatNotAvailableException;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.projections.WorkerBriefProjection;
import ai.openfabric.api.model.projections.WorkerStatProjection;
import ai.openfabric.api.repository.WorkerRepository;
import ai.openfabric.api.repository.WorkerStatsRepository;
import com.github.dockerjava.api.model.Container;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Component
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final WorkerStatsRepository workerStatsRepository;
    private final DockerService dockerService;


    public WorkerService(WorkerRepository workerRepository, WorkerStatsRepository workerStatsRepository, DockerService dockerService) {
        this.workerRepository = workerRepository;
        this.workerStatsRepository = workerStatsRepository;
        this.dockerService = dockerService;
    }

    public Worker getWorker(String id) throws NoSuchElementException {
        return workerRepository.findById(id).get();
    }

    public WorkerStatProjection getWorkerStat(String id) throws NoSuchElementException, WorkerStatNotAvailableException {
        LocalDateTime crt = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(crt);
        WorkerStatProjection stat = workerStatsRepository.findByWorkerId(id, timestamp.toString());
        if (stat == null) {
            throw new WorkerStatNotAvailableException();
        }
        return stat;
    }

    public Page<WorkerBriefProjection> getWorkersBrief(int page, int size) {
        return workerRepository.findAllBriefBy(PageRequest.of(page, size));
    }


    public Worker startWorker(String id) throws InterruptedException {
        Worker worker = getWorker(id);
        dockerService.startWorker(worker.getDockerId());
        Container container = dockerService.getContainer(worker.getDockerId());
        worker.setFromContainer(container);
        workerRepository.save(worker);
        return worker;
    }

    public Worker stopWorker(String id) throws InterruptedException {
        Worker worker = getWorker(id);
        dockerService.stopWorker(worker.getDockerId());
        Container container = dockerService.getContainer(worker.getDockerId());
        worker.setFromContainer(container);
        workerRepository.save(worker);
        return worker;
    }

    public void updateWorkers() {
        List<Container> containers = dockerService.getAllContainers();
        List<String> containerIds = new ArrayList<>();
        System.out.println("dfs " + containerIds);
        for (Container container : containers) {
            containerIds.add(container.getId());
        }
        workerRepository.deleteByDockerIdNotIn(containerIds);

        List<Worker> workers = new ArrayList<>();
        for (Container container : containers) {
            Worker worker = workerRepository.findByDockerId(container.getId());
            if (worker == null) {
                worker = new Worker();
            }
            worker.setFromContainer(container);
            workers.add(worker);
        }
        workerRepository.saveAll(workers);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // update workers on start-up
            updateWorkers();
        };
    }

}
