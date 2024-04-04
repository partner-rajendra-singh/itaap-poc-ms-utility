package com.philips.itaap.utility.model.deployment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderDetails {
    private String url;
    private String id;
    private String revision;
    private String name;
    private String folder;
}
