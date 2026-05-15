package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.WorkflowTransitionController;
import com.velocity_flow.api.dto.request.WorkflowTransitionCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowTransitionUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowTransitionOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.WorkflowTransitionOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WorkflowTransitionControllerImpl implements WorkflowTransitionController {

    private final WorkflowTransitionOperationService workflowTransitionService;

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_TRANSITION_CREATE)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> create(@RequestBody WorkflowTransitionCreateRequest createRequest) {
        return workflowTransitionService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_TRANSITION_UPDATE)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> update(@RequestBody WorkflowTransitionUpdateRequest updateRequest) {
        return workflowTransitionService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_TRANSITION_READ)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> get(Long id) {
        return workflowTransitionService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_TRANSITION_READ)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> getAll() {
        return workflowTransitionService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_TRANSITION_DELETE)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> Delete(Long id) {
        return workflowTransitionService.delete(id);
    }
}
