package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUserOperationResponse {
    private Long id;
    private Long projectId;
    private Long userId;
    private Long projectRoleId;
    private LocalDateTime assignedAt;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
