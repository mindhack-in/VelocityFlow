package com.velocity_flow.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskOperationResponse {
    private Long id;
    private Long projectId;
    private Long sprintId;
    private Long taskTypeId;
    private Long parentTaskId;
    private Integer taskLevel;
    private Integer rankOrder;
    private Long assigneeId;
    private String title;
    private String priority;
    private String status;
    private LocalDate dueDate;
    private List<Long> labelIds;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
