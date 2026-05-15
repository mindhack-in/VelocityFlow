package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransitionUpdateRequest {
    private Long id;
    private Long fromStateId;
    private Long toStateId;
}
