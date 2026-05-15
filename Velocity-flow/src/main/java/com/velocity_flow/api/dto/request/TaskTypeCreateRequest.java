package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTypeCreateRequest {
    private Long projectId;
    private Long workflowId;
    private String name;
    private String icon;
    private String color;
}
