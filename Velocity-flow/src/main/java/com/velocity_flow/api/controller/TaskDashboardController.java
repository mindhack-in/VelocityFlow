package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.GetAllEntity;
import com.velocity_flow.api.dto.request.WorkflowCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowUpdateRequest;
import com.velocity_flow.api.dto.response.TaskOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(TaskDashboardController.INSIGHTAPI)
public interface TaskDashboardController extends
		GetAllEntity<WorkflowCreateRequest, WorkflowUpdateRequest, BaseResponse<DataMap, TaskOperationResponse>> {
	public static final String INSIGHTAPI = "/task-dashboard";
}
