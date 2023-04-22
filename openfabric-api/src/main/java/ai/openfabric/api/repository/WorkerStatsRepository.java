package ai.openfabric.api.repository;

import ai.openfabric.api.model.WorkerStats;
import ai.openfabric.api.model.projections.WorkerStatProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkerStatsRepository extends JpaRepository<WorkerStats, String> {
    @Query(value = "SELECT read as latestRead,  " +
            "       memory_usage as latestMemoryUsage,  " +
            "       memory_max_usage as latestMemoryMaxUsage,  " +
            "       memory_fail_cnt as latestMemoryFailCnt,  " +
            "       memory_limit as latestMemoryLimit,  " +
            "       cpu_system_cpu_usage as latestCpuUsage,  " +
            "       cpu_online_cpus as latestOnlineCpus,  " +
            "       COALESCE((SELECT AVG(s1. memory_usage ) AS  aggregation  " +
            "                 FROM  worker_stats  s1  " +
            "                 WHERE s1. worker_id  = ( worker_stats . worker_id )  " +
            "                 AND s1. read  > to_timestamp(:time, 'YYYY-MM-DD HH24:MI:SS') - INTERVAL '5 minutes'  " +
            "                 GROUP BY s1. worker_id ), 0) AS  avgMemoryUsage5m ,  " +
            "         COALESCE((SELECT AVG(s1. cpu_system_cpu_usage ) AS  aggregation  " +
            "                    FROM  worker_stats  s1  " +
            "                    WHERE s1. worker_id  = ( worker_stats . worker_id )  " +
            "                    AND s1. read  > to_timestamp(:time, 'YYYY-MM-DD HH24:MI:SS') - INTERVAL '5 minutes'  " +
            "                    GROUP BY s1. worker_id ), 0) AS  avgCpuUsage5m ,  " +
            "            COALESCE((SELECT AVG(s1. memory_usage ) AS  aggregation  " +
            "                        FROM  worker_stats  s1  " +
            "                        WHERE s1. worker_id  = ( worker_stats . worker_id )  " +
            "                        AND s1. read  > to_timestamp(:time, 'YYYY-MM-DD HH24:MI:SS') - INTERVAL '30 minutes'  " +
            "                        GROUP BY s1. worker_id ), 0) AS  avgMemoryUsage30m ,  " +
            "            COALESCE((SELECT AVG(s1. cpu_system_cpu_usage ) AS  aggregation  " +
            "                        FROM  worker_stats  s1  " +
            "                        WHERE s1. worker_id  = ( worker_stats . worker_id )  " +
            "                        AND s1. read  > to_timestamp(:time, 'YYYY-MM-DD HH24:MI:SS') - INTERVAL '30 minutes'  " +
            "                        GROUP BY s1. worker_id ), 0) AS  avgCpuUsage30m  " +
            "FROM worker_stats  " +
            "         INNER JOIN (SELECT worker_id, MAX(read) AS latest_read  " +
            "                     FROM worker_stats  " +
            "                     GROUP BY worker_id) AS latest_read_stats  " +
            "                    ON worker_stats.worker_id = latest_read_stats.worker_id AND  " +
            "                       worker_stats.read = latest_read_stats.latest_read  " +
            "WHERE worker_stats.worker_id = :workerId LIMIT 1 ", nativeQuery = true)
    WorkerStatProjection findByWorkerId(@Param("workerId") String workerId, @Param("time") String timestamp);

}
