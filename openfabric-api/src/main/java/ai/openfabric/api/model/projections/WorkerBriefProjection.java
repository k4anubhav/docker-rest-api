package ai.openfabric.api.model.projections;

import ai.openfabric.api.model.WorkerName;

import java.util.Set;


public interface WorkerBriefProjection {
    String getId();

    String getUpdatedAt();

    String getCreatedAt();

    Set<WorkerName> getNames();

    String getDockerId();

    String getImage();

    String getStatus();

    String getState();

    String getCreated();

}
