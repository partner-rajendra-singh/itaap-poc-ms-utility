package com.philips.itaap.utility.serivce;

import com.philips.itaap.utility.dto.AccProdDTO;
import com.philips.itaap.utility.entity.AccProd;
import com.philips.itaap.utility.entity.Deployment;
import com.philips.itaap.utility.entity.Document;
import com.philips.itaap.utility.repository.AccProdRepo;
import com.philips.itaap.utility.repository.DeploymentRepo;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@XSlf4j
public class DocumentsService {

    @Autowired
    AccProdRepo accProdRepo;

    @Autowired
    DeploymentRepo deploymentRepo;

    public Mono<AccProd> insertDocumentsForBuildNumber(AccProdDTO dto) {

        Optional<AccProd> accProdOp = accProdRepo.findByStageBuildNumberPipelineID(dto.getBuildNumber(), dto.getStage(), dto.getPipelineID());
        Deployment deployment = deploymentRepo.getById(dto.getPipelineID());

        if (accProdOp.isPresent()) {
            Document document = accProdOp.get().getDocuments();
            switch (dto.getFileTag()) {
                case "ITD":
//                    deployment.setItd(dto.getFileData());
                    break;
                case "IQ":
//                    document.setIq(dto.getFileData());
                    break;
                case "PIQ":
//                    document.setPiq(dto.getFileData());
                    break;
                case "Azure Var":
//                    document.setAzureVariables(dto.getFileData());
                    break;
                case "Postman Coll.":
//                    deployment.setPostmanCollection(dto.getFileData());
                    break;
                default:
                    break;
            }
            AccProd accProd = accProdOp.get();
            accProd.setDocuments(document);

            accProd = accProdRepo.save(accProd);
            deploymentRepo.save(deployment);

            if (log.isInfoEnabled()) {
                log.info("UPDATED : DOCUMENT {} FOR {} BUILD NUMBER {}", dto.getFileName(), dto.getStage() == 3 ? "ACC" : "PROD", dto.getBuildNumber());
            }
            return Mono.just(accProd);
        } else {
            if (log.isInfoEnabled()) {
                log.info("NOT FOUND : ROW FOR DATA NOT FOUND");
            }
            return Mono.just(accProdOp.get());
        }
    }
}