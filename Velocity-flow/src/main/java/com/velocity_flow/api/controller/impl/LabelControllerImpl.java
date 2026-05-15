package com.velocity_flow.api.controller.impl;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velocity_flow.api.controller.LabelController;
import com.velocity_flow.api.dto.request.LabelCreateRequest;
import com.velocity_flow.api.dto.request.LabelUpdateRequest;
import com.velocity_flow.api.dto.response.LabelOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.service.LabelOperationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LabelControllerImpl implements LabelController {

    private final LabelOperationService labelService;

    @Override
    public BaseResponse<DataMap, LabelOperationResponse> create(@RequestBody LabelCreateRequest createRequest) {
        return labelService.create(createRequest);
    }

    @Override
    public BaseResponse<DataMap, LabelOperationResponse> update(@RequestBody LabelUpdateRequest updateRequest) {
        return labelService.update(updateRequest);
    }

    @Override
    public BaseResponse<DataMap, LabelOperationResponse> get(Long id) {
        return labelService.get(id);
    }

    @Override
    public BaseResponse<DataMap, LabelOperationResponse> getAll() {
        return labelService.getAll();
    }

    @Override
    public BaseResponse<DataMap, LabelOperationResponse> Delete(Long id) {
        return labelService.delete(id);
    }
}
