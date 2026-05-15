package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.RoleController;
import com.velocity_flow.api.dto.request.RoleCreateRequest;
import com.velocity_flow.api.dto.request.RoleUpdateRequest;
import com.velocity_flow.api.dto.response.RoleOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.RoleOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {

    private final RoleOperationService roleService;

    @Override
    @RequirePermission(PermissionConstants.ROLE_CREATE)
    public BaseResponse<DataMap, RoleOperationResponse> create(@RequestBody RoleCreateRequest createRequest) {
        return roleService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.ROLE_UPDATE)
    public BaseResponse<DataMap, RoleOperationResponse> update(@RequestBody RoleUpdateRequest updateRequest) {
        return roleService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.ROLE_READ)
    public BaseResponse<DataMap, RoleOperationResponse> get(Long id) {
        return roleService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.ROLE_READ)
    public BaseResponse<DataMap, RoleOperationResponse> getAll() {
        return roleService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.ROLE_DELETE)
    public BaseResponse<DataMap, RoleOperationResponse> Delete(Long id) {
        return roleService.delete(id);
    }
}
