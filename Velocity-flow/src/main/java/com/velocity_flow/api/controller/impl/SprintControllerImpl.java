package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.SprintController;
import com.velocity_flow.api.dto.request.SprintCreateRequest;
import com.velocity_flow.api.dto.request.SprintUpdateRequest;
import com.velocity_flow.api.dto.response.SprintOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.SprintOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SprintControllerImpl implements SprintController {

    private final SprintOperationService sprintService;

    @Override
    @RequirePermission(PermissionConstants.SPRINT_CREATE)
    public BaseResponse<DataMap, SprintOperationResponse> create(@RequestBody SprintCreateRequest createRequest) {
        return sprintService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.SPRINT_UPDATE)
    public BaseResponse<DataMap, SprintOperationResponse> update(@RequestBody SprintUpdateRequest updateRequest) {
        return sprintService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.SPRINT_READ)
    public BaseResponse<DataMap, SprintOperationResponse> get(Long id) {
        return sprintService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.SPRINT_READ)
    public BaseResponse<DataMap, SprintOperationResponse> getAll() {
        return sprintService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.SPRINT_DELETE)
    public BaseResponse<DataMap, SprintOperationResponse> Delete(Long id) {
        return sprintService.delete(id);
    }

	@Override
	public BaseResponse<DataMap, SprintOperationResponse> getByProjectId(Long projectId) {
        return sprintService.getByProjectId(projectId);

	}
}
