package com.philips.itaap.utility.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.philips.itaap.utility.model.enums.Role;
import com.philips.itaap.utility.validations.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersDTO {
    private Integer id;
    private String name;
    private String email;

    @ValueOfEnum(enumClass = Role.class, message = "'role' must be in [A, R, C, T, M, D]")
    private String role;
}
