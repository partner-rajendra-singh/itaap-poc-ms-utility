package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"task"})
public class Comments {
    @Id
    @GeneratedValue(generator = "seq_comments")
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "given_by_user_id")
    @JsonManagedReference
    @JsonIgnoreProperties({"role", "joiningDate", "designation", "accepted"})
    private User givenBy;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "timestamp")
    private Date timestamp;
    @ManyToOne(targetEntity = Task.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    @JsonBackReference
    private Task task;


}