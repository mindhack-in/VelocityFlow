package com.velocity_flow.api.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowCreateRequest {
    private Long projectId;
    private String workflowType;
    private String name;
    private Boolean reusable;
    private List<Long> stateIds;
    private List<Long> transitionIds;
}
