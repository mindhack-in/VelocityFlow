package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.WorkflowTransitionCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowTransitionUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowTransitionOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Workflow;
import com.velocity_flow.api.entity.WorkflowState;
import com.velocity_flow.api.entity.WorkflowTransition;
import com.velocity_flow.api.repository.WorkflowRepository;
import com.velocity_flow.api.repository.WorkflowStateRepository;
import com.velocity_flow.api.repository.WorkflowTransitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkflowTransitionOperationService {

    private final WorkflowTransitionRepository workflowTransitionRepository;
    private final WorkflowRepository workflowRepository;
    private final WorkflowStateRepository workflowStateRepository;

    @Transactional
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> create(WorkflowTransitionCreateRequest request) {
        WorkflowTransition transition = new WorkflowTransition();

        if (request.getWorkflowId() != null) {
            Workflow workflow = workflowRepository.findById(request.getWorkflowId())
                    .orElseThrow(() -> new RuntimeException("Workflow not found"));
            transition.setWorkflow(workflow);
        }

        if (request.getFromStateId() != null) {
            WorkflowState fromState = workflowStateRepository.findById(request.getFromStateId())
                    .orElseThrow(() -> new RuntimeException("FromState not found"));
            transition.setFromState(fromState);
        }

        if (request.getToStateId() != null) {
            WorkflowState toState = workflowStateRepository.findById(request.getToStateId())
                    .orElseThrow(() -> new RuntimeException("ToState not found"));
            transition.setToState(toState);
        }

        transition = workflowTransitionRepository.save(transition);

        BaseResponse<DataMap, WorkflowTransitionOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(transition));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> update(WorkflowTransitionUpdateRequest request) {
        WorkflowTransition transition = workflowTransitionRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("WorkflowTransition not found"));

        if (request.getFromStateId() != null) {
            WorkflowState fromState = workflowStateRepository.findById(request.getFromStateId())
                    .orElseThrow(() -> new RuntimeException("FromState not found"));
            transition.setFromState(fromState);
        }

        if (request.getToStateId() != null) {
            WorkflowState toState = workflowStateRepository.findById(request.getToStateId())
                    .orElseThrow(() -> new RuntimeException("ToState not found"));
            transition.setToState(toState);
        }

        transition = workflowTransitionRepository.save(transition);

        BaseResponse<DataMap, WorkflowTransitionOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(transition));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> get(Long id) {
        WorkflowTransition transition = workflowTransitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowTransition not found"));

        BaseResponse<DataMap, WorkflowTransitionOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(transition));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> getAll() {
        BaseResponse<DataMap, WorkflowTransitionOperationResponse> response = new BaseResponse<>(new DataMap());
        workflowTransitionRepository.findAll().forEach(transition -> response.getDataList().add(mapToDto(transition)));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, WorkflowTransitionOperationResponse> delete(Long id) {
        WorkflowTransition transition = workflowTransitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkflowTransition not found"));

        workflowTransitionRepository.delete(transition);

        BaseResponse<DataMap, WorkflowTransitionOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(transition));
        return response;
    }

    private WorkflowTransitionOperationResponse mapToDto(WorkflowTransition transition) {
        return new WorkflowTransitionOperationResponse(
                transition.getId(),
                transition.getWorkflow() != null ? transition.getWorkflow().getId() : null,
                transition.getFromState() != null ? transition.getFromState().getId() : null,
                transition.getToState() != null ? transition.getToState().getId() : null,
                transition.getCreatedOn(),
                transition.getUpdatedOn()
        );
    }
}
