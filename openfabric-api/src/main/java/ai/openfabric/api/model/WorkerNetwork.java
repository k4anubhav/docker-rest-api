package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Link;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity()
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"worker_id", "key"})})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorkerNetwork implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @JsonIgnore
    private Worker worker;

    @OneToMany(mappedBy = "workerNetwork", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<WorkerNetworkLink> links = new HashSet<>();

    @OneToMany(mappedBy = "workerNetwork", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<WorkerNetworkAlias> aliases = new HashSet<>();

    private String key;

    private String ip4Address;
    private String ip6Address;

    private String networkId;
    private String endpointId;
    private String gateway;
    private String ipAddress;
    private Integer ipPrefixLen;
    private String ipv6Gateway;
    private String globalIPv6Address;
    private Integer globalIPv6PrefixLen;
    private String macAddress;


    public void setFromContainerNetworkMapEntry(Map.Entry<String, ContainerNetwork> networkSetting) {
        this.key = networkSetting.getKey();

        if (networkSetting.getValue().getIpamConfig() != null) {
            this.ip4Address = networkSetting.getValue().getIpamConfig().getIpv4Address();
            this.ip6Address = networkSetting.getValue().getIpamConfig().getIpv6Address();
        }
        this.networkId = networkSetting.getValue().getNetworkID();
        this.endpointId = networkSetting.getValue().getEndpointId();
        this.gateway = networkSetting.getValue().getGateway();
        this.ipAddress = networkSetting.getValue().getIpAddress();
        this.ipPrefixLen = networkSetting.getValue().getIpPrefixLen();
        this.ipv6Gateway = networkSetting.getValue().getIpV6Gateway();
        this.globalIPv6Address = networkSetting.getValue().getGlobalIPv6Address();
        this.globalIPv6PrefixLen = networkSetting.getValue().getGlobalIPv6PrefixLen();
        this.macAddress = networkSetting.getValue().getMacAddress();

        if (networkSetting.getValue().getLinks() != null) {
            List<WorkerNetworkLink> links = new ArrayList<>();
            for (Link link : networkSetting.getValue().getLinks()) {
                WorkerNetworkLink workerNetworkLink = WorkerNetworkLink.builder().workerNetwork(this).build();
                workerNetworkLink.setFromLink(link);
                links.add(workerNetworkLink);
            }
            System.out.println("links: " + this.links);
            this.links.clear();
            this.links.addAll(links);
        }

        if (networkSetting.getValue().getAliases() != null) {
            List<WorkerNetworkAlias> aliases = new ArrayList<>();
            for (String alias : networkSetting.getValue().getAliases()) {
                WorkerNetworkAlias workerNetworkAlias = WorkerNetworkAlias.builder().workerNetwork(this).name(alias).build();
                aliases.add(workerNetworkAlias);
            }
            this.aliases.clear();
            this.aliases.addAll(aliases);
        }
    }
}
