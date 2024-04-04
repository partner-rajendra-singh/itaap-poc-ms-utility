package com.philips.itaap.utility.controller;


import com.philips.itaap.utility.dto.TaskDTO;
import com.philips.itaap.utility.entity.Task;
import com.philips.itaap.utility.exception.ServiceException;
import com.philips.itaap.utility.model.enums.Status;
import com.philips.itaap.utility.serivce.TasksService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @PostMapping(value = "/poc/create/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Task> createNewTask(@RequestBody @Valid TaskDTO tasks) {
        return tasksService.createNewTask(tasks);
    }

    @PutMapping(value = "/poc/update/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Task> updateTask(@RequestBody @Valid TaskDTO modifiedTask) {
        return tasksService.updateTask(modifiedTask);
    }

    @GetMapping(value = "/poc/fetch/tasks/all")
    public Mono<List<Task>> fetchAllTasks() {
        return tasksService.getAllTasks();
    }
    @GetMapping(value = "/poc/fetch/tasks/status")
    public Mono<List<Task>> getTasksByStatus(@RequestParam("status") String status) {
        return tasksService.getTasksByStatus(Status.valueOf(status));
    }
    @GetMapping(value = "/poc/fetch/tasks/user")
    public Mono<List<Task>> getTasksAssignedToUser(@RequestParam("userId") int userId) {
        return tasksService.getTasksAssignedToUser(userId);
    }
    @GetMapping(value = "/poc/fetch/tasks/created-by")
    public Mono<List<Task>> getTasksCreatedByUser(@RequestParam("userId") int userId) {
        return tasksService.getTasksCreatedByUser(userId);
    }
    @GetMapping(value = "/poc/fetch/tasks/microservice")
    public Mono<List<Task>> getTasksByMicroservice(@RequestParam("microserviceId") int microserviceId) {
        return tasksService.getTasksByMicroservice(microserviceId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public final @ResponseBody ServiceException handleValidationExceptions(WebExchangeBindException ex) {
        List<String> errorDesc = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        if (log.isErrorEnabled()) {
            log.error("handleValidationExceptions() : errorDesc -> {}", errorDesc);
        }
        throw new ServiceException(ex.getStatusCode(), ex.getStatusCode().value(), errorDesc.toString());
    }
}
