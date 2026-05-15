package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.ProjectRoleCreateRequest;
import com.velocity_flow.api.dto.request.ProjectRoleUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectRoleOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(ProjectRoleController.INSIGHTAPI)
public interface ProjectRoleController
        extends CreateEntity<ProjectRoleCreateRequest, ProjectRoleUpdateRequest, BaseResponse<DataMap, ProjectRoleOperationResponse>> {
    public static final String INSIGHTAPI = "/project-roles";
}
