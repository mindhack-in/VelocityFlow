package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.WorkflowStateCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowStateUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowStateOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(WorkflowStateController.INSIGHTAPI)
public interface WorkflowStateController
        extends CreateEntity<WorkflowStateCreateRequest, WorkflowStateUpdateRequest, BaseResponse<DataMap, WorkflowStateOperationResponse>> {
    public static final String INSIGHTAPI = "/workflow-states";
}
