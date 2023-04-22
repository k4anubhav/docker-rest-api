package ai.openfabric.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dockerjava.api.model.Statistics;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity()
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class WorkerStats implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "of-uuid")
    @GenericGenerator(name = "of-uuid", strategy = "ai.openfabric.api.model.IDGenerator")
    @JsonIgnore
    private String id;

    @ManyToOne()
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    @JsonIgnore
    private Worker worker;

    private Date read;
    private Long memoryUsage;
    private Long memoryMaxUsage;
    private Long memoryFailCnt;
    private Long memoryLimit;

    private Long cpuSystemCpuUsage;
    private Long cpuOnlineCpus;

    public void setFromStatistics(Statistics stats) throws ParseException {
        this.read = Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(stats.getRead())));
        this.memoryUsage = stats.getMemoryStats().getUsage();
        this.memoryMaxUsage = stats.getMemoryStats().getMaxUsage();
        this.memoryFailCnt = stats.getMemoryStats().getFailcnt();
        this.memoryLimit = stats.getMemoryStats().getLimit();

        this.cpuSystemCpuUsage = stats.getCpuStats().getSystemCpuUsage();
        this.cpuOnlineCpus = stats.getCpuStats().getOnlineCpus();
    }

}
