package ai.openfabric.api.repository;

import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.projections.WorkerBriefProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;

public interface WorkerRepository extends JpaRepository<Worker, String> {

    Worker getByDockerId(String dockerId);

    Worker findByDockerId(String dockerId);

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    Page<WorkerBriefProjection> findAllBriefBy(Pageable pageable);


    @Modifying
    @Transactional
    @Query("delete from Worker w where w.id not in :ids")
    void deleteByIdNotIn(Collection<String> ids);

    @Modifying
    @Transactional
    @Query("delete from Worker w where w.dockerId not in :ids")
    void deleteByDockerIdNotIn(Collection<String> ids);


}
