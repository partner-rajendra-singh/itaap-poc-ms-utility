package com.philips.itaap.utility.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "connectivity_history")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"triggeredBy"})
public class ConnectivityHistory {

    @Id
    @GeneratedValue(generator = "connectivity_history_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private String port;

    @Column(name = "triggered_by")
    private Integer triggeredBy;

    @Column(name = "triggered_at")
    private Date triggeredAt;

    @Column(name = "status")
    private String status;

    @Column(name = "PING_RESULT")
    private String pingResult;

    @Column(name = "SOCKET_RESULT")
    private String socketResult;

    @Column(name = "CURL_RESULT")
    private String curlResult;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "TRIGGERED_BY", insertable = false, updatable = false)
    @JsonManagedReference
    private User triggeredByUser;
}
