package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelOperationResponse {
    private Long id;
    private Long projectId;
    private String name;
    private String color;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
