package com.philips.itaap.utility.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS_ASSIGNED_TASKS")
@SuppressWarnings("PMD")
public class UserTask {
    @Id
    @GeneratedValue(generator = "users_tasks_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "TASK_ID")
    private Integer taskID;

    @NotNull
    @Column(name = "USER_ID")
    private Integer userID;

    @Column(name = "ACCEPTED")
    private Boolean accepted;
}