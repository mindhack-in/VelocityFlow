package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.ProjectUserCreateRequest;
import com.velocity_flow.api.dto.request.ProjectUserUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectUserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(ProjectUserController.INSIGHTAPI)
public interface ProjectUserController
        extends CreateEntity<ProjectUserCreateRequest, ProjectUserUpdateRequest, BaseResponse<DataMap, ProjectUserOperationResponse>> {
    public static final String INSIGHTAPI = "/project-users";
}
