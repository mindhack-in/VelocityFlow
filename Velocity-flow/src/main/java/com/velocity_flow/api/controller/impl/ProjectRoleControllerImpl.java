package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.ProjectRoleController;
import com.velocity_flow.api.dto.request.ProjectRoleCreateRequest;
import com.velocity_flow.api.dto.request.ProjectRoleUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectRoleOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.ProjectRoleOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectRoleControllerImpl implements ProjectRoleController {

    private final ProjectRoleOperationService projectRoleService;

    @Override
    @RequirePermission(PermissionConstants.PROJECT_ROLE_CREATE)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> create(@RequestBody ProjectRoleCreateRequest createRequest) {
        return projectRoleService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_ROLE_UPDATE)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> update(@RequestBody ProjectRoleUpdateRequest updateRequest) {
        return projectRoleService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_ROLE_READ)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> get(Long id) {
        return projectRoleService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_ROLE_READ)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> getAll() {
        return projectRoleService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_ROLE_DELETE)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> Delete(Long id) {
        return projectRoleService.delete(id);
    }
}
