package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.WorkflowController;
import com.velocity_flow.api.dto.request.WorkflowCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.WorkflowOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WorkflowControllerImpl implements WorkflowController {

    private final WorkflowOperationService workflowService;

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_CREATE)
    public BaseResponse<DataMap, WorkflowOperationResponse> create(@RequestBody WorkflowCreateRequest createRequest) {
        return workflowService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_UPDATE)
    public BaseResponse<DataMap, WorkflowOperationResponse> update(@RequestBody WorkflowUpdateRequest updateRequest) {
        return workflowService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_READ)
    public BaseResponse<DataMap, WorkflowOperationResponse> get(Long id) {
        return workflowService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_READ)
    public BaseResponse<DataMap, WorkflowOperationResponse> getAll() {
        return workflowService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.WORKFLOW_DELETE)
    public BaseResponse<DataMap, WorkflowOperationResponse> Delete(Long id) {
        return workflowService.delete(id);
    }

	@Override
	public BaseResponse<DataMap, WorkflowOperationResponse> getByProjectId(Long projectId) {
        return workflowService.getByProjectId(projectId);
	}
}
