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
@Table(name = "documents", schema = "poc")
public class Document {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "seq_documents")
    private Integer id;

//    @Column(name = "iq")
//    private String iq;

    @OneToOne(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "iq")
    private File iq;

//    @Column(name = "piq")
//    private String piq;

    @OneToOne(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "piq")
    private File piq;

    @Column(name = "sign_off_kt")
    private Date signOffKt;

    @Column(name = "sign_off_sit")
    private Date signOffSit;

    @Column(name = "sign_off_uat")
    private Date signOffUat;

    @Column(name = "sign_off_pt")
    private Date signOffPt;

    @Column(name = "sign_off_st")
    private Date signOffSt;

//    @Column(name = "azure_variables")
//    private String azureVariables;

    @OneToOne(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "azure_variables")
    private File azureVariables;
}