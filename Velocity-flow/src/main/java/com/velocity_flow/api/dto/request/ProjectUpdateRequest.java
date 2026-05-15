package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateRequest {
    private Long id;
    private Long organizationId;
    private String name;
    private String slug;
    private String status;
    private String visibility;
}
