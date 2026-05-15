package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationOperationResponse {
    private Long id;
    private String name;
    private String slug;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
