package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStateOperationResponse {
    private Long id;
    private Long workflowId;
    private String name;
    private Integer sequenceNo;
    private Boolean isInitial;
    private Boolean isFinal;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
