package com.philips.itaap.utility.serivce;

import com.philips.itaap.utility.config.AzureProperties;
import com.philips.itaap.utility.dto.MicroservicesDTO;
import com.philips.itaap.utility.entity.AccProd;
import com.philips.itaap.utility.entity.Microservices;
import com.philips.itaap.utility.repository.MicroservicesRepo;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@XSlf4j
@SuppressWarnings("CPD-START")
public class MsService {

    @Autowired
    private MicroservicesRepo microservicesRepo;

    @Autowired
    private AzureProperties properties;

    public Microservices createOrUpdateMicroservice(MicroservicesDTO dto) {
        Microservices microservice = Microservices.builder()
                .name(dto.getName())
                .techLeadID(dto.getTechLeadID())
                .developerID(dto.getDeveloperID())
                .backupID(dto.getBackupID())
                .scrumMasterID(dto.getScrumMasterID())
                .pocID(dto.getPocID())
                .status(dto.getStatus())
                .targetCompletionDate(dto.getTargetCompletionDate())
                .pipelineID(dto.getPipelineID())
                .build();

        Optional<Microservices> msOP = microservicesRepo.findByName(dto.getName());
        if (msOP.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("CREATED : MICROSERVICE -> [{}]", dto.getName());
            }
        } else {
            Microservices ms = msOP.get();
            microservice.setId(ms.getId());
            if (log.isInfoEnabled()) {
                log.info("UPDATED : MICROSERVICE -> [{}]", dto.getName());
            }
        }
        microservice = microservicesRepo.save(microservice);
        return microservice;
    }

    public Mono<List<Microservices>> getAllMicroservices() {
        List<Microservices> list = microservicesRepo.findAll().stream().filter(microservices -> microservices.getId() != 0).collect(Collectors.toList());
        list.forEach(microservices -> {
            if (microservices.getDeployment() != null) {
                List<AccProd> acc = microservices.getDeployment().getDeploymentAcc().stream().sorted(Comparator.comparing(AccProd::getBuildId).reversed()).collect(Collectors.toList());
                List<AccProd> prod = microservices.getDeployment().getDeploymentProd().stream().sorted(Comparator.comparing(AccProd::getBuildId).reversed()).collect(Collectors.toList());

                microservices.getDeployment().setDeploymentAcc(acc);
                microservices.getDeployment().setDeploymentProd(prod);
            }
        });
        return Mono.just(list)
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : MICROSERVICES : ALL");
                    }
                });
    }

    public Mono<Microservices> getMicroserviceById(Integer microserviceID) {
        return Mono.just(microservicesRepo.getById(microserviceID))
                .doOnSuccess(t -> {
                    if (log.isInfoEnabled()) {
                        log.info("FETCHED : MICROSERVICES : ID -> {}", microserviceID);
                    }
                });
    }

    public Mono<Optional<Microservices>> getMicroserviceByName(String name) {
        Optional<Microservices> op = microservicesRepo.findByName(name);
        if (op.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("FETCHED : MICROSERVICES : NAME NOT FOUND -> {}", name);
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("FETCHED : MICROSERVICES : NAME -> {}", name);
            }
        }
        return Mono.just(op);
    }

}
