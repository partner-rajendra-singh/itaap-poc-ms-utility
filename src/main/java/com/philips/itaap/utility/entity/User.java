package com.philips.itaap.utility.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "users_list_seq")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 1)
    @NotNull
    @Column(name = "role", nullable = false, length = 1)
    private String role;

    @OneToOne(targetEntity = File.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "AVATAR")
    private File avatar;

    @Column(name = "JOINING_DATE", nullable = false)
    private Date joiningDate;

    @Column(name = "DESIGNATION", nullable = false)
    private String designation;

    @Transient
    private Boolean accepted;

}