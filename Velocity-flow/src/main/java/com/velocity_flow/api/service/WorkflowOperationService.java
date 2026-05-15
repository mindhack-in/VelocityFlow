package com.velocity_flow.api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.request.WorkflowCreateRequest;
import com.velocity_flow.api.dto.request.WorkflowUpdateRequest;
import com.velocity_flow.api.dto.response.WorkflowOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.entity.Workflow;
import com.velocity_flow.api.repository.ProjectRepository;
import com.velocity_flow.api.repository.WorkflowRepository;
import com.velocity_flow.api.repository.WorkflowStateRepository;
import com.velocity_flow.api.repository.WorkflowTransitionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkflowOperationService {

	private final WorkflowRepository workflowRepository;
	private final ProjectRepository projectRepository;
	private final WorkflowStateRepository workflowStateRepository;
	private final WorkflowTransitionRepository workflowTransitionRepository;

	@Transactional
	public BaseResponse<DataMap, WorkflowOperationResponse> create(WorkflowCreateRequest request) {
		Workflow workflow = new Workflow();
		workflow.setWorkflowType(request.getWorkflowType());
		workflow.setName(request.getName());
		workflow.setReusable(request.getReusable());

		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new RuntimeException("Project not found"));
			workflow.setProject(project);
		}

		final Workflow savedWorkflow = workflowRepository.save(workflow);

		if (request.getStateIds() != null) {
			for (Long stateId : request.getStateIds()) {
				workflowStateRepository.findById(stateId).ifPresent(state -> {
					state.setWorkflow(savedWorkflow);
					workflowStateRepository.save(state);
				});
			}
		}

		if (request.getTransitionIds() != null) {
			for (Long transitionId : request.getTransitionIds()) {
				workflowTransitionRepository.findById(transitionId).ifPresent(transition -> {
					transition.setWorkflow(savedWorkflow);
					workflowTransitionRepository.save(transition);
				});
			}
		}

		BaseResponse<DataMap, WorkflowOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(savedWorkflow));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, WorkflowOperationResponse> update(WorkflowUpdateRequest request) {
		Workflow workflow = workflowRepository.findById(request.getId())
				.orElseThrow(() -> new RuntimeException("Workflow not found"));

		if (request.getWorkflowType() != null)
			workflow.setWorkflowType(request.getWorkflowType());
		if (request.getName() != null)
			workflow.setName(request.getName());
		if (request.getReusable() != null)
			workflow.setReusable(request.getReusable());

		workflow = workflowRepository.save(workflow);

		BaseResponse<DataMap, WorkflowOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(workflow));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, WorkflowOperationResponse> get(Long id) {
		Workflow workflow = workflowRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Workflow not found"));

		BaseResponse<DataMap, WorkflowOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(workflow));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, WorkflowOperationResponse> getAll() {
		BaseResponse<DataMap, WorkflowOperationResponse> response = new BaseResponse<>(new DataMap());
		workflowRepository.findAll().forEach(workflow -> response.getDataList().add(mapToDto(workflow)));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, WorkflowOperationResponse> delete(Long id) {
		Workflow workflow = workflowRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Workflow not found"));

		workflowRepository.delete(workflow);

		BaseResponse<DataMap, WorkflowOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(workflow));
		return response;
	}

	private WorkflowOperationResponse mapToDto(Workflow workflow) {
		return new WorkflowOperationResponse(workflow.getId(),
				workflow.getProject() != null ? workflow.getProject().getId() : null, workflow.getWorkflowType(),
				workflow.getName(), workflow.getReusable(), workflow.getCreatedOn(), workflow.getUpdatedOn());
	}

	public BaseResponse<DataMap, WorkflowOperationResponse> getByProjectId(Long projectId) {
		List<Workflow> workflow = workflowRepository.findByProjectIdIn(Arrays.asList(projectId));

		BaseResponse<DataMap, WorkflowOperationResponse> response = new BaseResponse<>(new DataMap());

		workflow.stream().forEach(w -> response.getDataList().add(mapToDto(w)));
		;

		return response;
	}
}
