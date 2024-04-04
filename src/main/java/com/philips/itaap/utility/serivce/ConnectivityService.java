package com.philips.itaap.utility.serivce;

import com.philips.itaap.utility.constant.ApplicationConstants;
import com.philips.itaap.utility.entity.ConnectivityHistory;
import com.philips.itaap.utility.entity.User;
import com.philips.itaap.utility.model.VerifyConnectionResponse;
import com.philips.itaap.utility.repository.ConnectivityHistoryRepo;
import com.philips.itaap.utility.repository.UserRepo;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@XSlf4j
public class ConnectivityService {

    @Autowired
    ConnectivityHistoryRepo connectivityHistoryRepo;

    public Integer saveHistory(Integer userID, String host, String port, String status, String pingResult, String curlResult, String socketResult) {
        ConnectivityHistory connectivityHistory = new ConnectivityHistory();
        connectivityHistory.setHost(host);
        connectivityHistory.setPort(port);
        connectivityHistory.setStatus(status);
        connectivityHistory.setTriggeredAt(new Date());
        connectivityHistory.setTriggeredBy(userID);
        connectivityHistory.setPingResult(pingResult);
        connectivityHistory.setCurlResult(curlResult);
        connectivityHistory.setSocketResult(socketResult);
        return connectivityHistoryRepo.save(connectivityHistory).getId();
    }

    public List<ConnectivityHistory> getAllHistory() {
        return connectivityHistoryRepo.findAllSorted();
    }

    public Optional<List<VerifyConnectionResponse>> getHistory(String id) {
        Optional<ConnectivityHistory> connectivityHistory = connectivityHistoryRepo.findById(Integer.parseInt(id));
        return connectivityHistory.map(history -> List.of(new VerifyConnectionResponse(ApplicationConstants.PING, history.getPingResult(), history.getStatus()),
                new VerifyConnectionResponse(ApplicationConstants.CURL, history.getCurlResult(), history.getStatus()),
                new VerifyConnectionResponse(ApplicationConstants.SOCKET, history.getSocketResult(), history.getStatus())));
    }
}
