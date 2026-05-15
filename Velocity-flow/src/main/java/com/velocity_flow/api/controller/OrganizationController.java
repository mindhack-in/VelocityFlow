package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.OrganizationCreateRequest;
import com.velocity_flow.api.dto.request.OrganizationUpdateRequest;
import com.velocity_flow.api.dto.response.OrganizationOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(OrganizationController.INSIGHTAPI)
public interface OrganizationController extends
		CreateEntity<OrganizationCreateRequest, OrganizationUpdateRequest, BaseResponse<DataMap, OrganizationOperationResponse>> {
	public static final String INSIGHTAPI = "/organizations";
}
