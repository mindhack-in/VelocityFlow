package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.GetByProjectEntity;
import com.velocity_flow.api.dto.request.WorkflowCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(WorkflowController.INSIGHTAPI)
public interface WorkflowController extends
		GetByProjectEntity<WorkflowCreateRequest, WorkflowUpdateRequest, BaseResponse<DataMap, WorkflowOperationResponse>> {
	public static final String INSIGHTAPI = "/workflows";

}
