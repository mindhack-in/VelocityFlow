package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.WorkflowTransitionCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowTransitionUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowTransitionOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(WorkflowTransitionController.INSIGHTAPI)
public interface WorkflowTransitionController
        extends CreateEntity<WorkflowTransitionCreateRequest, WorkflowTransitionUpdateRequest, BaseResponse<DataMap, WorkflowTransitionOperationResponse>> {
    public static final String INSIGHTAPI = "/workflow-transitions";
}
