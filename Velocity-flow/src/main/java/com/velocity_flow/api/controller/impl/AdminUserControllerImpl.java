package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.AdminUserController;
import com.velocity_flow.api.dto.request.UserCreateRequest;
import com.velocity_flow.api.dto.request.UserUpdateRequest;
import com.velocity_flow.api.dto.response.UserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.UserType;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.UserOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminUserControllerImpl implements AdminUserController {

	private final UserOperationService userService;

	@Override
	@RequirePermission(PermissionConstants.ADMIN_USER_CREATE)
	public BaseResponse<DataMap, UserOperationResponse> create(@RequestBody UserCreateRequest createRequest) {
		return userService.create(createRequest, UserType.ADMIN);
	}

	@Override
	@RequirePermission(PermissionConstants.ADMIN_USER_UPDATE)
	public BaseResponse<DataMap, UserOperationResponse> update(UserUpdateRequest updateRequest) {
		return userService.update(updateRequest);
	}

	@Override
	@RequirePermission(PermissionConstants.ADMIN_USER_READ)
	public BaseResponse<DataMap, UserOperationResponse> get(Long id) {
		return userService.get(id);
	}

	@Override
	@RequirePermission(PermissionConstants.ADMIN_USER_READ)
	public BaseResponse<DataMap, UserOperationResponse> getAll() {
		return userService.getAll(UserType.ADMIN);
	}

	@Override
	@RequirePermission(PermissionConstants.ADMIN_USER_DELETE)
	public BaseResponse<DataMap, UserOperationResponse> Delete(Long id) {
		return userService.delete(id);
	}
}
