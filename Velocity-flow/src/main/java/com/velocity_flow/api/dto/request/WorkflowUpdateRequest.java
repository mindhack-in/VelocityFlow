package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowUpdateRequest {
    private Long id;
    private String workflowType;
    private String name;
    private Boolean reusable;
}
