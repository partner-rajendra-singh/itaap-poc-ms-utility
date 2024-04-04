package com.philips.itaap.utility.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AzureProperties {

    @Value("${azure.pat}")
    String pat;

    @Value("${azure.var-grp-type}")
    String varGrpType;

    @Value("${azure.urls.get-variable-group}")
    String varGrpUrl;

    @Value("${azure.urls.get-all-variable-group}")
    String allGrpUrl;

    @Value("${azure.urls.update-variable-group}")
    String updVarGrpUrl;

    @Value("${azure.urls.add-variable-group}")
    String addVarGrpUrl;

    @Value("${azure.urls.delete-variable-group}")
    String deleteVarGrpUrl;

    @Value("${azure.urls.all-build-folders}")
    String allBuildFolderUrl;

    @Value("${azure.urls.runs-for-build-id}")
    String runsFromBuildID;

    @Value("${azure.urls.get-build-timeline}")
    String getBuildTimeline;

    @Value("${azure.org}")
    String org;
}
