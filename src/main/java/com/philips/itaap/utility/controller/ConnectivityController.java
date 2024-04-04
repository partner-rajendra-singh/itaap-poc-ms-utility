package com.philips.itaap.utility.controller;

import com.philips.itaap.utility.constant.ApplicationConstants;
import com.philips.itaap.utility.entity.ConnectivityHistory;
import com.philips.itaap.utility.model.VerifyConnectionRequest;
import com.philips.itaap.utility.model.VerifyConnectionResponse;
import com.philips.itaap.utility.serivce.ConnectivityService;
import com.philips.itaap.utility.serivce.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ConnectivityController {

    @Autowired
    private ConnectivityService connectivityService;

    @SuppressWarnings("rawtypes")
    @PostMapping("${api.paths.connectivity-check}")
    public ResponseEntity<List<VerifyConnectionResponse>> checkConnectivity(
            @RequestParam(name = "userID") Integer userID,
            @RequestBody VerifyConnectionRequest verifyConnectionRequest) {
        String status = "success";
        String pingResult = NetworkUtils.ping(verifyConnectionRequest.getHost());
        String socketResult = NetworkUtils.testConnect(verifyConnectionRequest.getHost(), verifyConnectionRequest.getPort());
        String curlResult = NetworkUtils.curl(verifyConnectionRequest.getHost());
        if (pingResult.contains("Ping request could not find host")) {
            status = "failed";
        }

        connectivityService.saveHistory(userID, verifyConnectionRequest.getHost(), verifyConnectionRequest.getPort(), status, pingResult, curlResult, socketResult);
        List<VerifyConnectionResponse> responseList = List.of(new VerifyConnectionResponse(ApplicationConstants.PING, pingResult, status),
                new VerifyConnectionResponse(ApplicationConstants.CURL, curlResult, status),
                new VerifyConnectionResponse(ApplicationConstants.SOCKET, socketResult, status));
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("${api.paths.connectivity-history}")
    public List<ConnectivityHistory> getAllHistory() {
        return connectivityService.getAllHistory();
    }

    @GetMapping("${api.paths.view-history}")
    public Optional<List<VerifyConnectionResponse>> getHistory(@PathVariable("id") String id) {
        return connectivityService.getHistory(id);
    }
}
