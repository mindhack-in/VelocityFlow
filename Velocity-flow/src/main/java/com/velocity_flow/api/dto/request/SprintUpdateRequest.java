package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintUpdateRequest {
    private Long id;
    private Long projectId;
    private Long workflowId;
    private String name;
    private Integer capacityHours;
    private Integer plannedStoryPoints;
    private Integer completedStoryPoints;
}
