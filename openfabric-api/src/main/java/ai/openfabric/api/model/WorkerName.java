package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerName implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @Getter
    @Setter
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @Getter
    @Setter
    @JsonIgnore
    private Worker worker;

    @Getter
    @Setter
    private String name;

    public WorkerName(Worker worker, String name) {
        this.worker = worker;
        this.name = name;
    }
}
