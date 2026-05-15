package com.velocity_flow.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {
    private Long id;
    private String name;
    private List<Long> permissionIds;
}
