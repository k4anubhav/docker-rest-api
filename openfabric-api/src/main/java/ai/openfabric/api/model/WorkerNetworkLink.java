package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.Link;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorkerNetworkLink implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_network_id", referencedColumnName = "id")
    @JsonIgnore
    private WorkerNetwork workerNetwork;

    private String name;
    private String alias;

    public void setFromLink(Link link) {
        this.name = link.getName();
        this.alias = link.getAlias();
    }
}
