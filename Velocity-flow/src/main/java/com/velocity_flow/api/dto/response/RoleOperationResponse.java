package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleOperationResponse {
    private Long id;
    private String name;
    private List<String> permissionsDescription;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
