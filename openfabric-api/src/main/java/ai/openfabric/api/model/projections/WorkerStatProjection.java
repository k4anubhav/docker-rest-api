package ai.openfabric.api.model.projections;

import java.util.Date;

public interface WorkerStatProjection {

    Date getLatestRead();

    Long getLatestMemoryUsage();

    Long getLatestMemoryMaxUsage();

    Long getLatestMemoryFailCnt();

    Long getLatestMemoryLimit();

    Long getLatestCpuUsage();

    Long getLatestOnlineCpus();

    Long getAvgMemoryUsage5m();

    Long getAvgCpuUsage5m();

    Long getAvgMemoryUsage30m();

    Long getAvgCpuUsage30m();
}
