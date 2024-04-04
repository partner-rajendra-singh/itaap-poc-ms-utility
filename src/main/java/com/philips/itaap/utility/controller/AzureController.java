package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.config.AzureProperties;
import com.philips.itaap.utility.entity.AzureVariablesEntity;
import com.philips.itaap.utility.model.ApprovalResponse;
import com.philips.itaap.utility.model.AzureVariablesRequest;
import com.philips.itaap.utility.model.UpdateRequest;
import com.philips.itaap.utility.model.Variables;
import com.philips.itaap.utility.serivce.AzureService;
import com.philips.itaap.utility.utils.Transformer;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import reactor.core.publisher.Mono;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class AzureController {

    @Autowired
    AzureService azureService;

    @Autowired
    AzureProperties properties;

    @PostMapping(value = "/poc/addVariableGroup")
    public Mono<String> addAzureVariableGroup(@RequestParam(name = "description") String description,
                                              @RequestParam(name = "name") String name,
                                              @RequestParam(name = "api-version") String apiVersion,
                                              @RequestParam(name = "organization") String organization,
                                              @RequestBody UpdateRequest updateRequest) {
        updateRequest.setDescription(description);
        updateRequest.setName(name);
        updateRequest.setType(properties.getVarGrpType());

        return azureService.addVariableGroup(organization, apiVersion, updateRequest);
    }

    @GetMapping(value = "/poc/getVariableGroup")
    public Mono<String> getAzureVariableGroup(@RequestParam(name = "groupName") String groupName,
                                              @RequestParam(name = "organization") String organization,
                                              @RequestParam(name = "api-version") String apiVersion) {
        return azureService.fetchVariableGroup(groupName, organization, apiVersion);
    }

    @PostMapping(value = "/poc/routeToApproval")
    public Mono<ApprovalResponse> routeToApproval(@RequestBody AzureVariablesRequest azureVariablesRequest) {

        if (log.isInfoEnabled()) {
            log.info("routeToApproval() : modifiedVarGrp -> {}", Transformer.getJSONString(azureVariablesRequest));
        }
        AzureVariablesEntity azureVariables = azureService.saveModifiedAzureVariables(azureVariablesRequest);

        String message = "Successfully routed the requested changes to the administrator of " + azureVariables.getAzureGroupName() + " group.";
        return Mono.just(new ApprovalResponse(message, HttpStatus.OK));
    }

    @GetMapping(value = "/poc/getVariableUsingGroupName")
    public Mono<Variables> getVariablesFromGroup(@RequestParam(name = "groupName") String groupName,
                                                 @RequestParam(name = "organization") String organization,
                                                 @RequestParam(name = "api-version") String apiVersion) {
        return azureService.fetchVariableGroup(groupName, organization, apiVersion)
                .map(string -> azureService.getVariablesFromGroup(string, groupName));
    }

    @GetMapping(value = "/poc/getAllVariableGroups")
    public Mono<String> getAllAzureVariableGroups(@RequestParam(name = "organization") String organization,
                                                  @RequestParam(name = "api-version") String apiVersion) {
        return azureService.fetchAllVariableGroups(organization, apiVersion);
    }

    @PutMapping(value = "/poc/updateVariableGroup")
    public Mono<String> updateAzureVariableGroup(@RequestParam(name = "groupID") String groupID,
                                                 @RequestParam(name = "description") String description,
                                                 @RequestParam(name = "name") String name,
                                                 @RequestParam(name = "api-version") String apiVersion,
                                                 @RequestParam(name = "organization") String organization,
                                                 @RequestBody UpdateRequest updateRequest) {
        updateRequest.setDescription(description);
        updateRequest.setName(name);
        updateRequest.setType(properties.getVarGrpType());

        return azureService.updateVariableGroup(organization, groupID, apiVersion, updateRequest);
    }

    @DeleteMapping(value = "/poc/deleteVariableGroup")
    public Mono<String> deleteAzureVariableGroup(@RequestParam(name = "groupID") String groupID,
                                                 @RequestParam(name = "api-version") String apiVersion,
                                                 @RequestParam(name = "organization") String organization) {

        return azureService.deleteVariableGroup(organization, groupID, apiVersion);
    }
}
