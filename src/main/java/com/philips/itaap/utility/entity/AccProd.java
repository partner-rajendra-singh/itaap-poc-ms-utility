package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"deployment", "documents"})
@Table(name = "acc_prod")
public class AccProd {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "seq_acc_prod")
    private Integer id;

    @Column(name = "stage")
    private Integer stage;

    @Column(name = "BUILD_NUMBER")
    private String buildNumber;

    @Column(name = "state")
    private String state;

    @Column(name = "deployment_date")
    private Date deploymentDate;

    @Column(name = "BUILD_ID")
    private Long buildId;

    @OneToOne(targetEntity = Document.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "documents")
    private Document documents;

    @ManyToOne(targetEntity = Deployment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "deployment_id")
    @JsonBackReference
    private Deployment deployment;

}