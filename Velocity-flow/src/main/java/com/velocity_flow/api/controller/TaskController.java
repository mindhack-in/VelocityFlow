package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.TaskCreateRequest;
import com.velocity_flow.api.dto.request.TaskUpdateRequest;
import com.velocity_flow.api.dto.response.TaskOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(TaskController.INSIGHTAPI)
public interface TaskController
        extends CreateEntity<TaskCreateRequest, TaskUpdateRequest, BaseResponse<DataMap, TaskOperationResponse>> {
    public static final String INSIGHTAPI = "/tasks";
}
