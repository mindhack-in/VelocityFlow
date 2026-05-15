package com.velocity_flow.api.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.request.TaskTypeCreateRequest;
import com.velocity_flow.api.dto.request.TaskTypeUpdateRequest;
import com.velocity_flow.api.dto.response.TaskTypeOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.entity.TaskType;
import com.velocity_flow.api.entity.Workflow;
import com.velocity_flow.api.repository.ProjectRepository;
import com.velocity_flow.api.repository.TaskTypeRepository;
import com.velocity_flow.api.repository.WorkflowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskTypeOperationService {

	private final TaskTypeRepository taskTypeRepository;
	private final ProjectRepository projectRepository;
	private final WorkflowRepository workflowRepository;

	@Transactional
	public BaseResponse<DataMap, TaskTypeOperationResponse> create(TaskTypeCreateRequest request) {
		TaskType taskType = new TaskType();
		taskType.setName(request.getName());
		taskType.setIcon(request.getIcon());
		taskType.setColor(request.getColor());

		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new RuntimeException("Project not found"));
			taskType.setProject(project);
		}

		if (request.getWorkflowId() != null) {
			Workflow workflow = workflowRepository.findById(request.getWorkflowId())
					.orElseThrow(() -> new RuntimeException("Workflow not found"));
			taskType.setWorkflow(workflow);
		}

		taskType = taskTypeRepository.save(taskType);

		BaseResponse<DataMap, TaskTypeOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(taskType));
		return response;
	}

	public BaseResponse<DataMap, TaskTypeOperationResponse> update(TaskTypeUpdateRequest request) {
		throw new UnsupportedOperationException("Operation not supported");
	}

	public BaseResponse<DataMap, TaskTypeOperationResponse> get(Long id) {
		throw new UnsupportedOperationException("Operation not supported");
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, TaskTypeOperationResponse> getAll() {
		BaseResponse<DataMap, TaskTypeOperationResponse> response = new BaseResponse<>(new DataMap());
		List<TaskType> list = taskTypeRepository.findAll();
		response.setDataList(list.stream().map(this::mapToDto).collect(Collectors.toList()));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, TaskTypeOperationResponse> delete(Long id) {
		TaskType taskType = taskTypeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("TaskType not found"));

		taskTypeRepository.delete(taskType);

		BaseResponse<DataMap, TaskTypeOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(taskType));
		return response;
	}

	private TaskTypeOperationResponse mapToDto(TaskType taskType) {
		return new TaskTypeOperationResponse(taskType.getId(),
				taskType.getProject() != null ? taskType.getProject().getId() : null,
				taskType.getWorkflow() != null ? taskType.getWorkflow().getId() : null, taskType.getName(),
				taskType.getIcon(), taskType.getColor(), taskType.getCreatedOn(), taskType.getUpdatedOn());
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, TaskTypeOperationResponse> getByProjectId(Long projectId) {
		BaseResponse<DataMap, TaskTypeOperationResponse> response = new BaseResponse<>(new DataMap());
		List<TaskType> list = taskTypeRepository.findByProjectIdIn(Arrays.asList(projectId));
		response.setDataList(list.stream().map(this::mapToDto).collect(Collectors.toList()));
		return response;
	}
}
