package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionOperationResponse {
    private Long id;
    private String permissionKey;
    private String description;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
