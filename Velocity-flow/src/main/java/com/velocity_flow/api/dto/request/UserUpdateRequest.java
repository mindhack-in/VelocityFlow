package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private Long id;
    private String name;
    private String email;
    private String status;
    private Long roleId;
    private Long organizationId;
}
