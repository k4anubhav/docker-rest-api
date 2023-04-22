package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.ContainerPort;
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
public class WorkerPort implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @JsonIgnore
    private Worker worker;


    private String ip;
    private Integer privatePort;
    private Integer publicPort;
    private String type;


    public void setFromContainerPort(ContainerPort containerPort) {
        this.ip = containerPort.getIp();
        this.privatePort = containerPort.getPrivatePort();
        this.publicPort = containerPort.getPublicPort();
        this.type = containerPort.getType();
    }
}
