package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransitionOperationResponse {
    private Long id;
    private Long workflowId;
    private Long fromStateId;
    private Long toStateId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
