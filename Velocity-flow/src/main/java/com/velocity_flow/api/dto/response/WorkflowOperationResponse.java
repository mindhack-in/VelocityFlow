package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowOperationResponse {
    private Long id;
    private Long projectId;
    private String workflowType;
    private String name;
    private Boolean reusable;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
