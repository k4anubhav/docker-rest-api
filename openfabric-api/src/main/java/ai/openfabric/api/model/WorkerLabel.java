package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity()
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"worker_id", "key"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorkerLabel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @JsonIgnore
    private Worker worker;

    private String key;
    private String value;

    public void setFromMapEntry(Map.Entry<String, String> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }
}
