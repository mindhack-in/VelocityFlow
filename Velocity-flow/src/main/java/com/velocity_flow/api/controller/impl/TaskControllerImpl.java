package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.TaskController;
import com.velocity_flow.api.dto.request.TaskCreateRequest;
import com.velocity_flow.api.dto.request.TaskUpdateRequest;
import com.velocity_flow.api.dto.response.TaskOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.TaskOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskControllerImpl implements TaskController {

    private final TaskOperationService taskService;

    @Override
    @RequirePermission(PermissionConstants.TASK_CREATE)
    public BaseResponse<DataMap, TaskOperationResponse> create(@RequestBody TaskCreateRequest createRequest) {
        return taskService.create(createRequest);
    }

    @Override
    public BaseResponse<DataMap, TaskOperationResponse> update(@RequestBody TaskUpdateRequest updateRequest) {
        return taskService.update(updateRequest);
    }

    @Override
    public BaseResponse<DataMap, TaskOperationResponse> get(Long id) {
        return taskService.get(id);
    }

    @Override
    public BaseResponse<DataMap, TaskOperationResponse> getAll() {
        return taskService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.TASK_DELETE)
    public BaseResponse<DataMap, TaskOperationResponse> Delete(Long id) {
        return taskService.delete(id);
    }

	@Override
	public BaseResponse<DataMap, TaskOperationResponse> updateStatus(Long id,String status) {
        return taskService.update(status,id);

	}
}
