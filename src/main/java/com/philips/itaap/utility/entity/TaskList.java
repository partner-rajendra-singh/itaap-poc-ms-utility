package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasklist")
@EqualsAndHashCode(exclude = {"task"})
@JsonIgnoreProperties({"completedByUserId"})
public class TaskList {
    @Id
    @GeneratedValue(generator = "seq_taskslist")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "task_details")
    private String taskDetails;

    @Column(name = "added_on")
    private Date addedOn;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "completed_by")
    @JsonManagedReference
    private User completedBy;

    @Column(name = "completed_on")
    private Date completedOn;

    @Column(name = "completed")
    private Boolean isCompleted;

    @ManyToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    @JsonBackReference
    private Task task;

}