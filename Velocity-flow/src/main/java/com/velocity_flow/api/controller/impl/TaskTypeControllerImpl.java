package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.TaskTypeController;
import com.velocity_flow.api.dto.request.TaskTypeCreateRequest;
import com.velocity_flow.api.dto.request.TaskTypeUpdateRequest;
import com.velocity_flow.api.dto.response.TaskTypeOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.TaskTypeOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskTypeControllerImpl implements TaskTypeController {

    private final TaskTypeOperationService taskTypeService;

    @Override
    @RequirePermission(PermissionConstants.TASK_TYPE_CREATE)
    public BaseResponse<DataMap, TaskTypeOperationResponse> create(@RequestBody TaskTypeCreateRequest createRequest) {
        return taskTypeService.create(createRequest);
    }

    @Override
    public BaseResponse<DataMap, TaskTypeOperationResponse> update(@RequestBody TaskTypeUpdateRequest updateRequest) {
        return taskTypeService.update(updateRequest);
    }

    @Override
    public BaseResponse<DataMap, TaskTypeOperationResponse> get(Long id) {
        return taskTypeService.get(id);
    }

    @Override
    public BaseResponse<DataMap, TaskTypeOperationResponse> getAll() {
        return taskTypeService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.TASK_TYPE_DELETE)
    public BaseResponse<DataMap, TaskTypeOperationResponse> Delete(Long id) {
        return taskTypeService.delete(id);
    }

	@Override
	public BaseResponse<DataMap, TaskTypeOperationResponse> getByProjectId(Long projectId) {
        return taskTypeService.getByProjectId(projectId);

	}
}
