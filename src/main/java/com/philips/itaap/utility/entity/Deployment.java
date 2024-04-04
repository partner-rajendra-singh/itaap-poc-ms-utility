package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deployment")
@EqualsAndHashCode(exclude = {"deploymentAcc", "deploymentProd"})
@SuppressWarnings("PMD")
public class Deployment {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(targetEntity = NonProd.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "deployment_dev")
    @JsonManagedReference
    @Where(clause = "stage = 1")
    private NonProd deploymentDev;

    @OneToOne(targetEntity = NonProd.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "deployment_test")
    @JsonManagedReference
    @Where(clause = "stage = 2")
    private NonProd deploymentTest;

    @OneToMany(mappedBy = "deployment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties({"deployment", "iq"})
    @Where(clause = "stage = 3")
    private List<AccProd> deploymentAcc;

    @OneToMany(mappedBy = "deployment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnoreProperties({"deployment", "piq"})
    @Where(clause = "stage = 4")
    private List<AccProd> deploymentProd;

    @Column(name = "tgl")
    private Date tgl;

    @Column(name = "bgl")
    private Date bgl;

//    @Column(name = "POSTMAN_COLLECTION")
//    private String postmanCollection;

//    @Column(name = "ITD")
//    private String itd;

    @OneToOne(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ITD")
    private File itd;

    @OneToOne(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "POSTMAN_COLLECTION")
    private File postmanCollection;

    @Column(name = "MICROSERVICE_ID")
    private Integer microserviceID;

}