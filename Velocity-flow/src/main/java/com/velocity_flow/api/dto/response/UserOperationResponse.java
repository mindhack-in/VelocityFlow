package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOperationResponse {
    private Long id;
    private String name;
    private String email;
    private String status;
    private Long roleId;
    private Long organizationId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedOn;
}
