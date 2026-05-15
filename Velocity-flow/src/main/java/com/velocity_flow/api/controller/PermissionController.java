package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.GetAllEntity;
import com.velocity_flow.api.dto.request.PermissionCreateRequest;
import com.velocity_flow.api.dto.request.PermissionUpdateRequest;
import com.velocity_flow.api.dto.response.PermissionOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(PermissionController.INSIGHTAPI)
public interface PermissionController extends
		GetAllEntity<PermissionCreateRequest, PermissionUpdateRequest, BaseResponse<DataMap, PermissionOperationResponse>> {
	public static final String INSIGHTAPI = "/permissions";

	public BaseResponse<DataMap, PermissionOperationResponse> get(Long id);
}
