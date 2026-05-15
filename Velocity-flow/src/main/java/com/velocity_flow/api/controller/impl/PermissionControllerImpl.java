package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.PermissionController;
import com.velocity_flow.api.dto.response.PermissionOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.PermissionOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PermissionControllerImpl implements PermissionController {

	private final PermissionOperationService permissionOperationService;

	@Override
	@RequirePermission(PermissionConstants.PERMISSION_READ)
	public BaseResponse<DataMap, PermissionOperationResponse> get(Long id) {
		return permissionOperationService.get(id);
	}

	@Override
	@RequirePermission(PermissionConstants.PERMISSION_READ)
	public BaseResponse<DataMap, PermissionOperationResponse> getAll() {
		return permissionOperationService.getAll();
	}

}
