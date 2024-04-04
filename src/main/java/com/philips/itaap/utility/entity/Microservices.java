package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "microservices")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("PMD")
public class Microservices {
    @Id
    @GeneratedValue(generator = "microservices_list_seq")
    @Column(name = "MICROSERVICE_ID", nullable = false)
    private Integer id;

    @Size(max = 1000)
    @NotNull
    @Column(name = "name", nullable = false, length = 1000)
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "target_completion_date")
    private Date targetCompletionDate;

    @Column(name = "tech_lead_id")
    private Integer techLeadID;

    @Column(name = "developer_id")
    private Integer developerID;

    @Column(name = "backup_id")
    private Integer backupID;

    @Column(name = "scrum_Master_id")
    private Integer scrumMasterID;

    @Column(name = "poc_id")
    private Integer pocID;

    @Column(name = "pipeline_id")
    private Integer pipelineID;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "tech_lead_id", insertable = false, updatable = false)
    @JsonManagedReference
    private User techLead;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "developer_id", insertable = false, updatable = false)
    @JsonManagedReference
    private User developer;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "backup_id", insertable = false, updatable = false)
    @JsonManagedReference
    private User backup;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "scrum_master_id", insertable = false, updatable = false)
    @JsonManagedReference
    private User scrumMaster;

    @NotNull
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "poc_id", insertable = false, updatable = false)
    @JsonManagedReference
    private User poc;

    @ManyToOne(targetEntity = Deployment.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "deployment_id")
    @JsonIgnoreProperties({"id"})
    @JsonManagedReference
    private Deployment deployment;

}