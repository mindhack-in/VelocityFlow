package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.GetByProjectEntity;
import com.velocity_flow.api.dto.request.TaskTypeCreateRequest;
import com.velocity_flow.api.dto.request.TaskTypeUpdateRequest;
import com.velocity_flow.api.dto.response.TaskTypeOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(TaskTypeController.INSIGHTAPI)
public interface TaskTypeController extends
		GetByProjectEntity<TaskTypeCreateRequest, TaskTypeUpdateRequest, BaseResponse<DataMap, TaskTypeOperationResponse>> {
	public static final String INSIGHTAPI = "/task-types";
}
