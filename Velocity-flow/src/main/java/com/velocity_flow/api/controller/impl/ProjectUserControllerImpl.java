package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.ProjectUserController;
import com.velocity_flow.api.dto.request.ProjectUserCreateRequest;
import com.velocity_flow.api.dto.request.ProjectUserUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectUserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.ProjectUserOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectUserControllerImpl implements ProjectUserController {

    private final ProjectUserOperationService projectUserService;

    @Override
    @RequirePermission(PermissionConstants.PROJECT_USER_CREATE)
    public BaseResponse<DataMap, ProjectUserOperationResponse> create(@RequestBody ProjectUserCreateRequest createRequest) {
        return projectUserService.create(createRequest);
    }

    @Override
    public BaseResponse<DataMap, ProjectUserOperationResponse> update(@RequestBody ProjectUserUpdateRequest updateRequest) {
        return projectUserService.update(updateRequest);
    }

    @Override
    public BaseResponse<DataMap, ProjectUserOperationResponse> get(Long id) {
        return projectUserService.get(id);
    }

    @Override
    public BaseResponse<DataMap, ProjectUserOperationResponse> getAll() {
        return projectUserService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.PROJECT_USER_DELETE)
    public BaseResponse<DataMap, ProjectUserOperationResponse> Delete(Long id) {
        return projectUserService.delete(id);
    }
}
