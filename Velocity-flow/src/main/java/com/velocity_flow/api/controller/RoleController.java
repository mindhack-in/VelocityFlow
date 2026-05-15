package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.RoleCreateRequest;
import com.velocity_flow.api.dto.request.RoleUpdateRequest;
import com.velocity_flow.api.dto.response.RoleOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(RoleController.INSIGHTAPI)
public interface RoleController
        extends CreateEntity<RoleCreateRequest, RoleUpdateRequest, BaseResponse<DataMap, RoleOperationResponse>> {
    public static final String INSIGHTAPI = "/roles";
}
