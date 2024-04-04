package com.philips.itaap.utility.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyConnectionRequest {

    private String host;

    private String port;
}
