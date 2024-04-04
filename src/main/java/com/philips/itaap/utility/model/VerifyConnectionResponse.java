package com.philips.itaap.utility.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyConnectionResponse {

    private String type;

    private String response;

    private String status;
}
