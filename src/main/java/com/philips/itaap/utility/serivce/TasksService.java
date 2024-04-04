package com.philips.itaap.utility.serivce;

import com.philips.itaap.utility.dto.TaskDTO;
import com.philips.itaap.utility.entity.*;
import com.philips.itaap.utility.model.enums.Status;
import com.philips.itaap.utility.repository.*;
import com.philips.itaap.utility.utils.Transformer;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@XSlf4j
@SuppressWarnings("CPD-START")
public class TasksService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TasksRepo tasksRepo;
    @Autowired
    private CommentsRepo commentsRepo;
    @Autowired
    private TaskListRepo taskListRepo;
    @Autowired
    private UserTasksRepo userTasksRepo;

    public Mono<Task> createNewTask(TaskDTO dto) {

        log.info("createNewTask() : taskDTO -> {}", Transformer.getJSONString(dto));

        if (dto.getMicroserviceID() == null) {
            dto.setMicroserviceID(0);
        }
        Set<User> assignees = new HashSet<>();
        dto.getAssignedUsers().forEach(userId -> assignees.add(userRepo.getById(userId)));

        Task task = Task.builder()
                .taskTitle(dto.getTaskTitle())
                .taskDesc(dto.getTaskDesc())
                .status(Status.N.name())
                .priority(dto.getPriority())
                .assignees(assignees)
                .microserviceID(dto.getMicroserviceID())
                .createdDate(Calendar.getInstance().getTime())
                .createdByUserID(dto.getCreatedByUserID())
                .startDate(dto.getStartDate())
                .dueDate(dto.getDueDate())
                .build();

        task = tasksRepo.save(task);

        Task finalTask = task;
        dto.getTaskLists().forEach(taskListDTO -> {
            TaskList taskList = TaskList.builder()
                    .taskDetails(taskListDTO.getTaskDetails())
                    .addedOn(taskListDTO.getAddedOn())
                    .isCompleted(taskListDTO.isCompleted())
                    .task(finalTask)
                    .build();
            if (taskListDTO.isCompleted()) {
                taskList.setCompletedOn(taskListDTO.getCompletedOn());
                taskList.setCompletedBy(userRepo.getById(taskListDTO.getCompletedByUserId()));
            }
            taskListRepo.save(taskList);
        });

        return Mono
                .just(tasksRepo.save(task))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("CREATED : TASK -> [{}]", dto.getTaskTitle());
                    }
                });
    }

    public Mono<List<Task>> getAllTasks() {
        return Mono.just(fetchAllTasks())
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : TASKS : ALL");
                    }
                });
    }

    public Mono<List<Task>> getTasksByStatus(Status status) {
        List<Task> tasks = fetchAllTasks();
        return Mono.just(tasks.stream().filter(task -> status.name().equals(task.getStatus())).collect(Collectors.toList()))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : TASKS : BY STATUS -> {}", status.value());
                    }
                });
    }

    public Mono<List<Task>> getTasksAssignedToUser(int userId) {
        List<Task> tasks =
                fetchAllTasks()
                        .stream()
                        .filter(task -> task.getAssignees().stream().anyMatch(user -> user.getId() == userId))
                        .collect(Collectors.toList());

        return Mono.just(tasks)
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : TASKS : BY USER");
                    }
                });
    }

    public Mono<List<Task>> getTasksCreatedByUser(int userId) {
        List<Task> tasks = fetchAllTasks();
        return Mono.just(tasks.stream().filter(task -> task.getCreatedBy().getId() == userId).collect(Collectors.toList()))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : TASKS : CREATED BY USER");
                    }
                });
    }

    public Mono<List<Task>> getTasksByMicroservice(int microserviceId) {
        List<Task> tasks = fetchAllTasks();
        return Mono.just(tasks.stream().filter(task -> task.getMicroservices().getId() == microserviceId).collect(Collectors.toList()))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : TASKS : BY MICROSERVICE");
                    }
                });
    }

    public Mono<Task> updateTask(TaskDTO modifiedTask) {
        Task task = tasksRepo.getById(modifiedTask.getId());
        if (modifiedTask.getAssignedUsers() != null && !modifiedTask.getAssignedUsers().isEmpty()) {
            Set<User> assignees = new HashSet<>();
            modifiedTask.getAssignedUsers().forEach(userId -> assignees.add(userRepo.getById(userId)));
            task.setAssignees(assignees);
        }
        if (!modifiedTask.getComments().isEmpty()) {
            modifiedTask.getComments().forEach(commentsDTO -> {
                if (commentsDTO.getId() == null || commentsDTO.getId() == 0) {
                    Comments comment = Comments.builder()
                            .comments(commentsDTO.getComments())
                            .timestamp(commentsDTO.getTimestamp())
                            .givenBy(userRepo.getById(commentsDTO.getGivenByUserId()))
                            .task(task)
                            .build();
                    commentsRepo.save(comment);
                }
            });
        }
        task.setTaskDesc(modifiedTask.getTaskDesc());
        task.setStatus(modifiedTask.getStatus());
        if (Status.C.name().equals(modifiedTask.getStatus())) {
            task.setCompletedDate(Calendar.getInstance().getTime());
        } else {
            task.setModifiedDate(Calendar.getInstance().getTime());
        }
        if (Status.A.name().equals(modifiedTask.getStatus())) {
            Optional<UserTask> userTaskOp = userTasksRepo.findByTaskAndUserId(modifiedTask.getId(), modifiedTask.getAcceptedByUserID());
            if (userTaskOp.isPresent()) {
                UserTask userTask = userTaskOp.get();
                userTask.setAccepted(true);
                userTasksRepo.save(userTask);
            }
        }
        task.setCompletionComments(modifiedTask.getCompletionComments());
        task.setNotes(modifiedTask.getNotes());
        task.setImpediments(modifiedTask.getImpediments());

        return Mono.just(tasksRepo.save(task))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("UPDATED : TASK : {}", task.getId());
                    }
                });
    }


    private List<Task> fetchAllTasks() {
        List<Task> tasks = tasksRepo.findAll();
        List<UserTask> userTasks = userTasksRepo.findAll();

        tasks.forEach(task -> {
            task.getAssignees().forEach(user ->
                    userTasks
                            .stream()
                            .filter(userTask -> Objects.equals(userTask.getUserID(), user.getId()) && Objects.equals(userTask.getTaskID(), task.getId()))
                            .collect(Collectors.toList())
                            .forEach(userTask -> user.setAccepted(userTask.getAccepted()))
            );
            if (!task.getStatus().equals(Status.C.name()) && task.getDueDate().before(Calendar.getInstance().getTime())) {
                task.setStatus(Status.O.name());
            }
            task.setComments(task.getComments().stream().sorted((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp())).collect(Collectors.toList()));
        });
        return tasks;
    }
}
