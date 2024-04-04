package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.dto.AccProdDTO;
import com.philips.itaap.utility.entity.AccProd;
import com.philips.itaap.utility.serivce.DocumentsService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@XSlf4j
@SuppressWarnings({"CPD-START"})
public class DocumentsController {

    @Autowired
    private DocumentsService documentsService;

    @PostMapping(value = "/poc/update/documents", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<AccProd> updateDocuments(@RequestBody AccProdDTO accProdDTO) {
        return documentsService.insertDocumentsForBuildNumber(accProdDTO);
    }
}
