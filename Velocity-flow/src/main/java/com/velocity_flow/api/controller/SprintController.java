package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.GetByProjectEntity;
import com.velocity_flow.api.dto.request.SprintCreateRequest;
import com.velocity_flow.api.dto.request.SprintUpdateRequest;
import com.velocity_flow.api.dto.response.SprintOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(SprintController.INSIGHTAPI)
public interface SprintController extends
		GetByProjectEntity<SprintCreateRequest, SprintUpdateRequest, BaseResponse<DataMap, SprintOperationResponse>> {
	public static final String INSIGHTAPI = "/sprints";
}
