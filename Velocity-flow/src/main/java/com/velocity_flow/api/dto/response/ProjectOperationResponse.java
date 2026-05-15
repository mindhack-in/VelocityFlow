package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectOperationResponse {
    private Long id;
    private Long organizationId;
    private String name;
    private String slug;
    private String status;
    private String visibility;
    private LocalDateTime createdAt;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
