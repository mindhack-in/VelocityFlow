package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.OrganizationController;
import com.velocity_flow.api.dto.request.OrganizationCreateRequest;
import com.velocity_flow.api.dto.request.OrganizationUpdateRequest;
import com.velocity_flow.api.dto.response.OrganizationOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.OrganizationOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrganizationControllerImpl implements OrganizationController {

    private final OrganizationOperationService organizationService;

    @Override
    @RequirePermission(PermissionConstants.ORGANIZATION_CREATE)
    public BaseResponse<DataMap, OrganizationOperationResponse> create(@RequestBody OrganizationCreateRequest createRequest) {
        return organizationService.create(createRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.ORGANIZATION_UPDATE)
    public BaseResponse<DataMap, OrganizationOperationResponse> update(@RequestBody OrganizationUpdateRequest updateRequest) {
        return organizationService.update(updateRequest);
    }

    @Override
    @RequirePermission(PermissionConstants.ORGANIZATION_READ)
    public BaseResponse<DataMap, OrganizationOperationResponse> get(Long id) {
        return organizationService.get(id);
    }

    @Override
    @RequirePermission(PermissionConstants.ORGANIZATION_READ)
    public BaseResponse<DataMap, OrganizationOperationResponse> getAll() {
        return organizationService.getAll();
    }

    @Override
    @RequirePermission(PermissionConstants.ORGANIZATION_DELETE)
    public BaseResponse<DataMap, OrganizationOperationResponse> Delete(Long id) {
        return organizationService.delete(id);
    }
}
