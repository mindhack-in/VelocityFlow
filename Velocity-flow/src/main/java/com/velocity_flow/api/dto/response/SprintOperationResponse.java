package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintOperationResponse {
    private Long id;
    private Long projectId;
    private String name;
    private Integer capacityHours;
    private Integer plannedStoryPoints;
    private Integer completedStoryPoints;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
