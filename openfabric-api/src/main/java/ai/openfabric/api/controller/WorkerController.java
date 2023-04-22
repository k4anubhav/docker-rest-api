package ai.openfabric.api.controller;

import ai.openfabric.api.exception.*;
import ai.openfabric.api.model.Worker;
import ai.openfabric.api.model.projections.WorkerBriefProjection;
import ai.openfabric.api.model.projections.WorkerStatProjection;
import ai.openfabric.api.service.WorkerService;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotModifiedException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("${node.api.path}/worker")
public class WorkerController {

    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final int MAX_PAGE_SIZE = 10;

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }


    @GetMapping(path = "/workers")
    public @ResponseBody Page<WorkerBriefProjection> workerList(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }
        if (size < 1) {
            size = DEFAULT_PAGE_SIZE;
        }
        if (page < 0) {
            page = 0;
        }
        return workerService.getWorkersBrief(page, size);
    }

    @GetMapping(path = "/workers/{id}")
    public @ResponseBody Worker worker(@PathVariable String id) {
        return workerService.getWorker(id);
    }

    @PostMapping(path = "/workers/{id}/start")
    public @ResponseBody Worker startWorker(@PathVariable String id) {
        try {
            return workerService.startWorker(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/workers/{id}/stop")
    public @ResponseBody Worker stopWorker(@PathVariable String id) {
        try {
            return workerService.stopWorker(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/workers/{id}/stats")
    public @ResponseBody WorkerStatProjection getWorkerStats(@PathVariable String id) throws WorkerStatNotAvailableException {
        return workerService.getWorkerStat(id);
    }


    @ExceptionHandler(NotModifiedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody WorkerNotModifiedErrorResponse handleNotModifiedException(NotModifiedException e) {
        return new WorkerNotModifiedErrorResponse();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody NotFoundErrorResponse handleNoSuchElementException(NoSuchElementException e) {
        return new NotFoundErrorResponse();
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody DockerInternalServerErrorResponse handleInternalServerErrorException(InternalServerErrorException e) {
        return new DockerInternalServerErrorResponse(e);
    }

    @ExceptionHandler(WorkerStatNotAvailableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody NotAvailableResponse handleWorkerStatNotAvailableException(WorkerStatNotAvailableException e) {
        return new NotAvailableResponse();
    }

}
