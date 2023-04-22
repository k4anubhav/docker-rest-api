package ai.openfabric.api.exception;

import com.github.dockerjava.api.exception.InternalServerErrorException;

public class DockerInternalServerErrorResponse extends ErrorResponse {
    public DockerInternalServerErrorResponse(InternalServerErrorException e) {
        super(e.getMessage(), "docker_internal_server_error");
    }
}
