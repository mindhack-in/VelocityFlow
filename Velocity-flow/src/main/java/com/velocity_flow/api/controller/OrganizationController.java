package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.UserCreateRequest;
import com.velocity_flow.api.dto.request.UserUpdateRequest;
import com.velocity_flow.api.dto.response.UserOperationResponse;

@RequestMapping(OrganizationController.INSIGHTAPI)
public interface OrganizationController
		extends CreateEntity<UserCreateRequest, UserUpdateRequest, UserOperationResponse> {
	public static final String INSIGHTAPI = "/organization-user";

}
