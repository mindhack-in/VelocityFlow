package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStateCreateRequest {
    private Long workflowId;
    private String name;
    private Integer sequenceNo;
    private Boolean isInitial;
    private Boolean isFinal;
}
