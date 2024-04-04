package com.philips.itaap.utility.model.deployment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BuildFolder {
    private String label;
    private String data;
    private List<String> path;
    private String expandedIcon = "pi pi-folder-open";
    private String collapsedIcon = "pi pi-folder";
    private String icon;
    private List<BuildFolder> children;

    public static BuildFolder newChild(String label, String data, List<BuildFolder> children) {
        BuildFolder folder = new BuildFolder();
        folder.label = label;
        folder.data = data;
        folder.setChildren(children);
        return folder;
    }

    public static BuildFolder newChild(List<String> path, String label, String data) {
        BuildFolder folder = new BuildFolder();
        folder.label = label;
        folder.data = data;
        folder.path = path;
        folder.icon = "pi pi-cloud";
        folder.collapsedIcon = null;
        folder.expandedIcon = null;
        return folder;
    }
}
