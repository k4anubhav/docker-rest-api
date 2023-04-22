package ai.openfabric.api.model;


import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerMount;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ContainerPort;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity()
@NoArgsConstructor()
@AllArgsConstructor()
@Builder
@Getter
@Setter
public class Worker extends Datable implements Serializable {

    @OneToMany(mappedBy = "worker", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerPort> ports = new HashSet<>();

    @OneToMany(mappedBy = "worker", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerName> names = new HashSet<>();

    @OneToMany(mappedBy = "worker", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerLabel> labels = new HashSet<>();

    @OneToMany(mappedBy = "worker", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerNetwork> networks = new HashSet<>();

    @OneToMany(mappedBy = "worker", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkerMount> mounts = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    private String id;

    @Column(unique = true, nullable = false)
    private String dockerId;

    private String command;

    private Date created;

    private String image;

    private String imageId;

    private String status;
    private String state;

    private Long sizeRw;

    private Long sizeRootFs;

    private String hostConfigNetworkMode;


    public void setFromContainer(Container container) {
        this.dockerId = container.getId();
        this.command = container.getCommand();
        this.created = new Date(container.getCreated() * 1000);
        this.image = container.getImage();
        this.imageId = container.getImageId();
        this.status = container.getStatus();
        this.state = container.getState();

        List<WorkerPort> workerPorts = new ArrayList<>();
        for (ContainerPort port : container.getPorts()) {
            WorkerPort workerPort = WorkerPort.builder().worker(this).build();
            workerPort.setFromContainerPort(port);
            workerPorts.add(workerPort);
        }
        this.ports.clear();
        this.ports.addAll(workerPorts);

        List<WorkerName> workerNames = new ArrayList<>();
        for (String name : container.getNames()) {
            workerNames.add(WorkerName.builder().worker(this).name(name).build());
        }
        this.names.clear();
        this.names.addAll(workerNames);

        List<WorkerLabel> workerLabels = new ArrayList<>();
        for (Map.Entry<String, String> label : container.getLabels().entrySet()) {
            WorkerLabel workerLabel = WorkerLabel.builder().worker(this).build();
            workerLabel.setFromMapEntry(label);
            workerLabels.add(workerLabel);
        }
        this.labels.clear();
        this.labels.addAll(workerLabels);

        this.sizeRw = container.getSizeRw();
        this.sizeRootFs = container.getSizeRootFs();
        if (container.getHostConfig() != null) this.hostConfigNetworkMode = container.getHostConfig().getNetworkMode();

        List<WorkerMount> workerMounts = new ArrayList<>();
        for (ContainerMount mount : container.getMounts()) {
            WorkerMount workerMount = WorkerMount.builder().worker(this).build();
            workerMount.setFromContainerMount(mount);
            workerMounts.add(workerMount);
        }
        this.mounts.clear();
        this.mounts.addAll(workerMounts);

        if (container.getNetworkSettings() != null && container.getNetworkSettings().getNetworks() != null) {
            List<WorkerNetwork> workerNetworks = new ArrayList<>();
            for (Map.Entry<String, ContainerNetwork> networkSetting : container.getNetworkSettings().getNetworks().entrySet()) {
                WorkerNetwork workerNetwork = WorkerNetwork.builder().worker(this).build();
                workerNetwork.setFromContainerNetworkMapEntry(networkSetting);
                workerNetworks.add(workerNetwork);
            }
            this.networks.clear();
            this.networks.addAll(workerNetworks);
        }

    }

}

