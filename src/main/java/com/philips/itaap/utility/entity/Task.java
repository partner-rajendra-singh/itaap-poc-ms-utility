package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@JsonIgnoreProperties({"microserviceID", "userID", "createdByUserID"})
@SuppressWarnings("PMD")
public class Task {
    @Id
    @GeneratedValue(generator = "tasks_list_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_description")
    private String taskDesc;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "microservice_id")
    private Integer microserviceID;

    @OneToMany(targetEntity = User.class)
    @JoinTable(name = "USERS_ASSIGNED_TASKS", joinColumns = @JoinColumn(name = "TASK_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @JsonManagedReference
    @JsonIgnoreProperties({"role", "joiningDate", "designation"})
    private Set<User> assignees;

    @OneToMany(targetEntity = File.class)
    @JoinTable(name = "ATTACHMENTS_TASK_FILES", joinColumns = @JoinColumn(name = "TASK_ID"), inverseJoinColumns = @JoinColumn(name = "FILES_ID"))
    @JsonManagedReference
    private Set<File> attachments;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties("tasks")
    private List<Comments> comments;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties("tasks")
    private List<TaskList> taskLists;

    @Column(name = "created_by")
    private Integer createdByUserID;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    @JsonManagedReference
    @JsonIgnoreProperties({"role", "joiningDate", "designation", "accepted"})
    private User createdBy;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "completed_date")
    private Date completedDate;

    @Column(name = "completion_comments")
    private String completionComments;

    @Column(name = "notes")
    private String notes;

    @Column(name = "impediments")
    private String impediments;

    @OneToOne(targetEntity = Microservices.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "microservice_id", insertable = false, updatable = false)
    @JsonManagedReference
    @JsonIgnoreProperties({"backup", "scrumMaster", "poc", "deployment", "signOff", "documents", "techLeadID", "developerID", "status", "targetCompletionDate"})
    private Microservices microservices;

}
