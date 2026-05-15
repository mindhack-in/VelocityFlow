package com.velocity_flow.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.velocity_flow.api.controller.base.CreateEntity;
import com.velocity_flow.api.dto.request.LabelCreateRequest;
import com.velocity_flow.api.dto.request.LabelUpdateRequest;
import com.velocity_flow.api.dto.response.LabelOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;

@RequestMapping(LabelController.INSIGHTAPI)
public interface LabelController
        extends CreateEntity<LabelCreateRequest, LabelUpdateRequest, BaseResponse<DataMap, LabelOperationResponse>> {
    public static final String INSIGHTAPI = "/labels";
}
