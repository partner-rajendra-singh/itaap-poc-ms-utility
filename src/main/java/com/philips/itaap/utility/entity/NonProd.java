package com.philips.itaap.utility.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "non_prod")
public class NonProd {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "seq_non_prod")
    private Integer id;

    @Column(name = "stage")
    private Integer stage;

    @Column(name = "MICROSERVICE_ID")
    private Integer microserviceID;

    @Column(name = "BUILD_NUMBER")
    private String buildNumber;

    @Column(name = "deployment_date")
    private Date deploymentDate;

    @Column(name = "state")
    private String state;

    @Column(name = "BUILD_ID")
    private Long buildId;
}