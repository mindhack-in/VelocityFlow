package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.UserCreateRequest;
import com.velocity_flow.api.dto.request.UserUpdateRequest;
import com.velocity_flow.api.dto.response.UserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(AdminUserController.INSIGHTAPI)
public interface AdminUserController
		extends CreateEntity<UserCreateRequest, UserUpdateRequest, BaseResponse<DataMap, UserOperationResponse>> {
	public static final String INSIGHTAPI = "/admin-user";

}
