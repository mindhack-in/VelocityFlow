package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.ProjectController;
import com.velocity_flow.api.dto.request.ProjectCreateRequest;
import com.velocity_flow.api.dto.request.ProjectUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.ProjectOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectControllerImpl implements ProjectController {

    private final ProjectOperationService projectService;

    @Override
    @RequirePermission(PermissionConstants.PROJECT_CREATE)
    public BaseResponse<DataMap, ProjectOperationResponse> create(@RequestBody ProjectCreateRequest createRequest) {
        return projectService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_UPDATE)
    public BaseResponse<DataMap, ProjectOperationResponse> update(@RequestBody ProjectUpdateRequest updateRequest) {
        return projectService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_READ)
    public BaseResponse<DataMap, ProjectOperationResponse> get(Long id) {
        return projectService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_READ)
    public BaseResponse<DataMap, ProjectOperationResponse> getAll() {
        return projectService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_DELETE)
    public BaseResponse<DataMap, ProjectOperationResponse> Delete(Long id) {
        return projectService.delete(id);
    }
}
