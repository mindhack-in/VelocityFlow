package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.MemberUserController;
import com.velocity_flow.api.dto.request.UserCreateRequest;
import com.velocity_flow.api.dto.request.UserUpdateRequest;
import com.velocity_flow.api.dto.response.UserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.security.annotation.RequirePermission;
import com.velocity_flow.api.service.UserOperationService;
import com.velocity_flow.api.utils.PermissionConstants;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberUserControllerImpl implements MemberUserController {

	private final UserOperationService userService;

	@Override
	@RequirePermission(PermissionConstants.MEMBER_USER_CREATE)
	public BaseResponse<DataMap, UserOperationResponse> create(@RequestBody UserCreateRequest createRequest) {
		return userService.create(createRequest);
	}

	@Override
	@RequirePermission(PermissionConstants.MEMBER_USER_UPDATE)
	public BaseResponse<DataMap, UserOperationResponse> update(UserUpdateRequest updateRequest) {
		return userService.update(updateRequest);
	}

	@Override
	@RequirePermission(PermissionConstants.MEMBER_USER_READ)
	public BaseResponse<DataMap, UserOperationResponse> get(Long id) {
		return userService.get(id);
	}

	@Override
	@RequirePermission(PermissionConstants.MEMBER_USER_READ)
	public BaseResponse<DataMap, UserOperationResponse> getAll() {
		return userService.getAll();
	}

	@Override
	@RequirePermission(PermissionConstants.MEMBER_USER_DELETE)
	public BaseResponse<DataMap, UserOperationResponse> Delete(Long id) {
		return userService.delete(id);
	}
}
