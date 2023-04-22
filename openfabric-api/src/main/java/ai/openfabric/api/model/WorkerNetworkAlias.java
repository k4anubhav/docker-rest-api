package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorkerNetworkAlias {
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
}
