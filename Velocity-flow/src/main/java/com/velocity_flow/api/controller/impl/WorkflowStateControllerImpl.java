package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.WorkflowStateController;
import com.velocity_flow.api.dto.request.WorkflowStateCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowStateUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowStateOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.WorkflowStateOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WorkflowStateControllerImpl implements WorkflowStateController {

    private final WorkflowStateOperationService workflowStateService;

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_STATE_CREATE)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> create(@RequestBody WorkflowStateCreateRequest createRequest) {
        return workflowStateService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_STATE_UPDATE)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> update(@RequestBody WorkflowStateUpdateRequest updateRequest) {
        return workflowStateService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_STATE_READ)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> get(Long id) {
        return workflowStateService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_STATE_READ)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> getAll() {
        return workflowStateService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_STATE_DELETE)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> Delete(Long id) {
        return workflowStateService.delete(id);
    }
}
