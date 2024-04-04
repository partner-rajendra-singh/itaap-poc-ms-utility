package com.philips.itaap.utility.serivce;

import com.fasterxml.jackson.core.type.TypeReference;
import com.philips.itaap.ms.dev.base.exception.ServiceException;
import com.philips.itaap.utility.config.AzureProperties;
import com.philips.itaap.utility.constant.AzureConstants;
import com.philips.itaap.utility.constant.RaquestStatus;
import com.philips.itaap.utility.entity.AzureVariablesEntity;
import com.philips.itaap.utility.model.*;
import com.philips.itaap.utility.repository.AzureVariablesRepo;
import com.philips.itaap.utility.utils.Transformer;
import lombok.extern.slf4j.XSlf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@XSlf4j
@SuppressWarnings({"PMD", "CPD-START"})
public class AzureService {

    @Autowired
    WebClient webClient;

    @Autowired
    AzureProperties properties;

    @Autowired
    AzureVariablesRepo azureVariablesRepo;

    public Mono<String> fetchVariableGroup(String groupName, String organization, String apiVersion) {
        String uri = properties.getVarGrpUrl().replace(AzureConstants.ORG_PLACEHOLDER, organization);
        WebClient.ResponseSpec requestBodySpec = webClient
                .get()
                .uri(uri, uriBuilder -> {
                    uriBuilder.queryParam(AzureConstants.GROUP_NAME, groupName);
                    uriBuilder.queryParam(AzureConstants.API_VERSION_NAME, apiVersion);
                    return uriBuilder.build();
                })
                .headers(getConsumerHeader())
                .retrieve();

        return getOnStatus(requestBodySpec)
                .bodyToMono(String.class)
                .doOnSuccess(s -> log.info("fetchVariableGroup() : Variable Group -> {}", s));
    }

    public Mono<String> updateVariableGroup(String organization, String id, String apiVersion, UpdateRequest updateRequest) {
        String uri = properties.getUpdVarGrpUrl().replace(AzureConstants.ORG_PLACEHOLDER, organization).replace(AzureConstants.ID_PLACEHOLDER, id);
        WebClient.ResponseSpec responseSpec = getResponseSpec(webClient.put(), uri, apiVersion, updateRequest);
        return getOnStatus(responseSpec).bodyToMono(String.class);
    }

    public Mono<String> addVariableGroup(String organization, String apiVersion, UpdateRequest updateRequest) {
        String uri = properties.getAddVarGrpUrl().replace(AzureConstants.ORG_PLACEHOLDER, organization);
        WebClient.ResponseSpec responseSpec = getResponseSpec(webClient.post(), uri, apiVersion, updateRequest);
        return getOnStatus(responseSpec).bodyToMono(String.class);
    }

    public Mono<String> deleteVariableGroup(String organization, String groupID, String apiVersion) {
        String uri = properties.getUpdVarGrpUrl().replace(AzureConstants.ORG_PLACEHOLDER, organization).replace(AzureConstants.ID_PLACEHOLDER, groupID);
        WebClient.ResponseSpec responseSpec = webClient
                .delete()
                .uri(uri, getUriBuilder(apiVersion))
                .headers(getConsumerHeader())
                .retrieve();

        return getOnStatus(responseSpec).bodyToMono(String.class);
    }

    public Mono<String> fetchAllVariableGroups(String organization, String apiVersion) {
        String uri = properties.getAddVarGrpUrl().replace(AzureConstants.ORG_PLACEHOLDER, organization);
        WebClient.ResponseSpec requestBodySpec = webClient
                .get()
                .uri(uri, uriBuilder -> uriBuilder.queryParam(AzureConstants.API_VERSION_NAME, apiVersion).build())
                .headers(getConsumerHeader())
                .retrieve();
        return getOnStatus(requestBodySpec)
                .bodyToMono(String.class)
                .doOnSuccess(s -> log.info("fetchAllVariableGroups() : Variable Groups"));
    }

    private Consumer<HttpHeaders> getConsumerHeader() {
        return httpHeaders -> httpHeaders.setBasicAuth(StringUtils.EMPTY, properties.getPat());
    }

    /**
     * This method is to create a URI Builder.
     *
     * @param apiVersion String
     * @return Function
     */
    private Function<UriBuilder, URI> getUriBuilder(String apiVersion) {
        return uriBuilder -> {
            uriBuilder.queryParam(AzureConstants.API_VERSION_NAME, apiVersion);
            return uriBuilder.build();
        };
    }

    /**
     * This method is used to prepare RequestBodySpec for WebClient.
     *
     * @param bodyUriSpec   WebClient Request URI Spec
     * @param uri           String
     * @param apiVersion    String
     * @param updateRequest UpdateRequest
     * @return WebClient Response Spec
     */
    private WebClient.ResponseSpec getResponseSpec(WebClient.RequestBodyUriSpec bodyUriSpec, String uri, String apiVersion, UpdateRequest updateRequest) {
        return bodyUriSpec
                .uri(uri, getUriBuilder(apiVersion))
                .headers(getConsumerHeader())
                .bodyValue(updateRequest)
                .retrieve();
    }

    /**
     * This method is used to capture exception if status is 4xx/5xx exception is thrown else ResponseSpec is returned.
     *
     * @param responseSpec {@link WebClient.ResponseSpec}
     * @return {@link WebClient.ResponseSpec}
     */
    private WebClient.ResponseSpec getOnStatus(WebClient.ResponseSpec responseSpec) {
        return responseSpec.onStatus(HttpStatus::isError, response ->
                response
                        .bodyToMono(String.class)
                        .flatMap(error -> {
                            log.info("Exception Occurred -> {}", error);
                            return Mono.error(new ServiceException(response.statusCode(), response.rawStatusCode(), error));
                        }));
    }

    public Variables getVariablesFromGroup(String group, String groupName) {
        AzureResponse<UpdateRequest> response = Transformer.readValue(group, new TypeReference<>() {
        });
        List<VariableValues> list = new ArrayList<>();
        AtomicInteger i = new AtomicInteger(1);
        if (response.getCount() != 0) {
            response.getValue().get(0).getVariables().forEach((key, variableValues) -> {
//                boolean isSecret = Base64.isBase64(variableValues.getValue());
                list.add(new VariableValues(i.getAndIncrement(), key, variableValues.getValue(), false, false));
            });
        }
        AtomicInteger count = new AtomicInteger();

        list.forEach(variableValues -> {
            if (variableValues.isSecret()) {
                count.getAndIncrement();
            }
        });
        return new Variables(groupName, count.get(), list);
    }

    public AzureVariablesEntity saveModifiedAzureVariables(AzureVariablesRequest azureVariablesRequest) {

        AzureVariablesEntity azureVariablesEntity = new AzureVariablesEntity();
        azureVariablesEntity.setRequester(azureVariablesRequest.getRequester());
        azureVariablesEntity.setAzureGroupName(azureVariablesRequest.getAzureGroupName());
        azureVariablesEntity.setApprover(azureVariablesRequest.getApprover());
        azureVariablesEntity.setUpdatedAllVariables(Transformer.getJSONString(azureVariablesRequest.getUpdatedAllVariables()));
        azureVariablesEntity.setModifiedVariables(Transformer.getJSONString(azureVariablesRequest.getModifiedVariables()));

        azureVariablesEntity.setRequestDate(Calendar.getInstance().getTime());
        azureVariablesEntity.setStatus(RaquestStatus.INITIATED.value());

        return azureVariablesRepo.save(azureVariablesEntity);
    }

    public List<AzureVariablesEntity> getRequests(String approver) {
        return azureVariablesRepo
                .findByApprover(approver)
                .stream()
                .filter(azureVariablesEntity -> azureVariablesEntity.getStatus().equals(RaquestStatus.INITIATED.value()))
                .collect(Collectors.toList());
    }

    public int approveRequest(AzureVariablesRequest request) {
        return azureVariablesRepo.updateStatusById(request.getId(), RaquestStatus.APPROVED.value());

    }

    public int rejectRequest(AzureVariablesRequest request) {
        return azureVariablesRepo.updateStatusById(request.getId(), RaquestStatus.REJECTED.value());
    }

    public List<AzureVariablesEntity> getMyRequests(String requester) {
        return new ArrayList<>(azureVariablesRepo
                .findByRequester(requester));
    }

}