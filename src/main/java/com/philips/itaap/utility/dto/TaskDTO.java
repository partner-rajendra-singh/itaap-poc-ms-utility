package com.philips.itaap.utility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.philips.itaap.utility.model.enums.Priority;
import com.philips.itaap.utility.model.enums.Status;
import com.philips.itaap.utility.validations.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {
    private Integer id;
    private String taskTitle;
    private String taskDesc;

    @ValueOfEnum(enumClass = Priority.class)
    private String priority;

    @ValueOfEnum(enumClass = Status.class, message = "'status' must be in [N, A, R, C, O, P]")
    private String status;
    private Integer microserviceID;
    private Integer createdByUserID;
    private Integer acceptedByUserID;
    private List<Integer> assignedUsers;
    private List<CommentsDTO> comments;
    private List<TaskListDTO> taskLists;
    private Date dueDate;
    private Date startDate;
    private String completionComments;
    private String notes;
    private String impediments;
}
