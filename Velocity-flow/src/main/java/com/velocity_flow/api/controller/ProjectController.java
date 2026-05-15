package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.ProjectCreateRequest;
import com.velocity_flow.api.dto.request.ProjectUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(ProjectController.INSIGHTAPI)
public interface ProjectController
        extends CreateEntity<ProjectCreateRequest, ProjectUpdateRequest, BaseResponse<DataMap, ProjectOperationResponse>> {
    public static final String INSIGHTAPI = "/projects";
}
