package com.philips.itaap.utility.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(generator = "seq_files")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_extension")
    private String fileExtension;

    @Column(name = "data")
    private String data;
}