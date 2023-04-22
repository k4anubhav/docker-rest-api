package ai.openfabric.api.repository;

import ai.openfabric.api.model.WorkerNetwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerNetworkRepository extends JpaRepository<WorkerNetwork, String> {
}
