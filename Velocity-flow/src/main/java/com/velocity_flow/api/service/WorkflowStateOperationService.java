package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.WorkflowStateCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowStateUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowStateOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Workflow;
import com.velocity_flow.api.entity.WorkflowState;
import com.velocity_flow.api.repository.WorkflowRepository;
import com.velocity_flow.api.repository.WorkflowStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkflowStateOperationService {

    private final WorkflowStateRepository workflowStateRepository;
    private final WorkflowRepository workflowRepository;

    @Transactional
    public BaseResponse<DataMap, WorkflowStateOperationResponse> create(WorkflowStateCreateRequest request) {
        WorkflowState state = new WorkflowState();
        state.setName(request.getName());
        state.setSequenceNo(request.getSequenceNo());
        state.setIsInitial(request.getIsInitial());
        state.setIsFinal(request.getIsFinal());

        if (request.getWorkflowId() != null) {
            Workflow workflow = workflowRepository.findById(request.getWorkflowId())
                    .orElseThrow(() -> new RuntimeException("Workflow not found"));
            state.setWorkflow(workflow);
        }

        state = workflowStateRepository.save(state);

        BaseResponse<DataMap, WorkflowStateOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(state));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, WorkflowStateOperationResponse> update(WorkflowStateUpdateRequest request) {
        WorkflowState state = workflowStateRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("WorkflowState not found"));

        if (request.getName() != null) state.setName(request.getName());
        if (request.getSequenceNo() != null) state.setSequenceNo(request.getSequenceNo());
        if (request.getIsInitial() != null) state.setIsInitial(request.getIsInitial());
        if (request.getIsFinal() != null) state.setIsFinal(request.getIsFinal());

        state = workflowStateRepository.save(state);

        BaseResponse<DataMap, WorkflowStateOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(state));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> get(Long id) {
        WorkflowState state = workflowStateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowState not found"));

        BaseResponse<DataMap, WorkflowStateOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(state));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, WorkflowStateOperationResponse> getAll() {
        BaseResponse<DataMap, WorkflowStateOperationResponse> response = new BaseResponse<>(new DataMap());
        workflowStateRepository.findAll().forEach(state -> response.getDataList().add(mapToDto(state)));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, WorkflowStateOperationResponse> delete(Long id) {
        WorkflowState state = workflowStateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowState not found"));

        workflowStateRepository.delete(state);

        BaseResponse<DataMap, WorkflowStateOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(state));
        return response;
    }

    private WorkflowStateOperationResponse mapToDto(WorkflowState state) {
        return new WorkflowStateOperationResponse(
                state.getId(),
                state.getWorkflow() != null ? state.getWorkflow().getId() : null,
                state.getName(),
                state.getSequenceNo(),
                state.getIsInitial(),
                state.getIsFinal(),
                state.getCreatedOn(),
                state.getUpdatedOn()
        );
    }
}
