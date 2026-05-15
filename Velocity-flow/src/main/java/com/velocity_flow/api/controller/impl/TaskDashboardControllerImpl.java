package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.TaskDashboardController;
import com.velocity_flow.api.dto.response.TaskOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskDashboardControllerImpl implements TaskDashboardController {

	private final DashboardService dashboardService;

	@Override
	public BaseResponse<DataMap, TaskOperationResponse> getAll() {
		// TODO Auto-generated method stub
		return dashboardService.getDashboardDetails();
	}

}
