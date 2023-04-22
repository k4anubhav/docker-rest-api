package ai.openfabric.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.ContainerMount;
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
public class WorkerMount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @JsonIgnore
    private Worker worker;

    private String name;
    private String source;
    private String destination;
    private String driver;
    private String mode;
    private boolean rw;
    private String propagation;

    public void setFromContainerMount(ContainerMount containerMount) {
        this.name = containerMount.getName();
        this.source = containerMount.getSource();
        this.destination = containerMount.getDestination();
        this.driver = containerMount.getDriver();
        this.mode = containerMount.getMode();
        this.rw = Boolean.TRUE.equals(containerMount.getRw());
        this.propagation = containerMount.getPropagation();
    }
}
