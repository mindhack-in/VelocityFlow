package com.velocity_flow.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.request.TaskCreateRequest;
import com.velocity_flow.api.dto.request.TaskUpdateRequest;
import com.velocity_flow.api.dto.response.TaskOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Label;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.entity.Sprint;
import com.velocity_flow.api.entity.Task;
import com.velocity_flow.api.entity.TaskType;
import com.velocity_flow.api.entity.User;
import com.velocity_flow.api.entity.WorkflowState;
import com.velocity_flow.api.entity.WorkflowTransition;
import com.velocity_flow.api.exception.InvalidWorkflowTransitionException;
import com.velocity_flow.api.repository.LabelRepository;
import com.velocity_flow.api.repository.ProjectRepository;
import com.velocity_flow.api.repository.SprintRepository;
import com.velocity_flow.api.repository.TaskRepository;
import com.velocity_flow.api.repository.TaskTypeRepository;
import com.velocity_flow.api.repository.UserRepository;
import com.velocity_flow.api.repository.WorkflowStateRepository;
import com.velocity_flow.api.repository.WorkflowTransitionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskOperationService {

	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;
	private final SprintRepository sprintRepository;
	private final TaskTypeRepository taskTypeRepository;
	private final UserRepository userRepository;
	private final LabelRepository labelRepository;
	private final WorkflowStateRepository workflowStateRepository;
	private final WorkflowTransitionRepository workflowTransitionRepository;

	@Transactional
	public BaseResponse<DataMap, TaskOperationResponse> create(TaskCreateRequest request) {
		Task task = new Task();
		task.setTaskLevel(request.getTaskLevel());
		task.setRankOrder(request.getRankOrder());
		task.setTitle(request.getTitle());
		task.setPriority(request.getPriority());
		task.setStatus(request.getStatus() != null ? request.getStatus() : "OPEN");
		task.setDueDate(request.getDueDate());

		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new RuntimeException("Project not found"));
			task.setProject(project);
		}

		if (request.getSprintId() != null) {
			Sprint sprint = sprintRepository.findById(request.getSprintId())
					.orElseThrow(() -> new RuntimeException("Sprint not found"));
			task.setSprint(sprint);
		}

		if (request.getTaskTypeId() != null) {
			TaskType taskType = taskTypeRepository.findById(request.getTaskTypeId())
					.orElseThrow(() -> new RuntimeException("TaskType not found"));
			task.setTaskType(taskType);
		}

		if (request.getParentTaskId() != null) {
			Task parentTask = taskRepository.findById(request.getParentTaskId())
					.orElseThrow(() -> new RuntimeException("Parent Task not found"));
			task.setParentTask(parentTask);
		}

		if (request.getAssigneeId() != null) {
			User assignee = userRepository.findById(request.getAssigneeId())
					.orElseThrow(() -> new RuntimeException("User not found"));
			task.setAssignee(assignee);
		}

		if (request.getLabelIds() != null && !request.getLabelIds().isEmpty()) {
			List<Label> labels = labelRepository.findAllById(request.getLabelIds());
			task.setLabels(new HashSet<>(labels));
		}

		task = taskRepository.save(task);

		BaseResponse<DataMap, TaskOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(task));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, TaskOperationResponse> update(TaskUpdateRequest request) {
		Task task = taskRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Task not found"));

		if (request.getTaskLevel() != null)
			task.setTaskLevel(request.getTaskLevel());
		if (request.getRankOrder() != null)
			task.setRankOrder(request.getRankOrder());
		if (request.getTitle() != null)
			task.setTitle(request.getTitle());
		if (request.getPriority() != null)
			task.setPriority(request.getPriority());
		if (request.getDueDate() != null)
			task.setDueDate(request.getDueDate());

		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new RuntimeException("Project not found"));
			task.setProject(project);
		}

		if (request.getSprintId() != null) {
			Sprint sprint = sprintRepository.findById(request.getSprintId())
					.orElseThrow(() -> new RuntimeException("Sprint not found"));
			task.setSprint(sprint);
		}

		if (request.getTaskTypeId() != null) {
			TaskType taskType = taskTypeRepository.findById(request.getTaskTypeId())
					.orElseThrow(() -> new RuntimeException("TaskType not found"));
			task.setTaskType(taskType);
		}

		if (request.getParentTaskId() != null) {
			Task parentTask = taskRepository.findById(request.getParentTaskId())
					.orElseThrow(() -> new RuntimeException("Parent Task not found"));
			task.setParentTask(parentTask);
		}

		if (request.getAssigneeId() != null) {
			User assignee = userRepository.findById(request.getAssigneeId())
					.orElseThrow(() -> new RuntimeException("User not found"));
			task.setAssignee(assignee);
		}

		if (request.getLabelIds() != null) {
			List<Label> labels = labelRepository.findAllById(request.getLabelIds());
			task.setLabels(new HashSet<>(labels));
		}

		task = taskRepository.save(task);

		BaseResponse<DataMap, TaskOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(task));
		return response;
	}

	public BaseResponse<DataMap, TaskOperationResponse> get(Long id) {
		throw new UnsupportedOperationException("Operation not supported");
	}

	public BaseResponse<DataMap, TaskOperationResponse> getAll() {
		BaseResponse<DataMap, TaskOperationResponse> response = new BaseResponse<DataMap, TaskOperationResponse>();
		List<Task> user = taskRepository.findAll();

		response.setDataList(user.stream().map(this::mapToDto).collect(Collectors.toList()));

		return response;
	}

	@Transactional
	public BaseResponse<DataMap, TaskOperationResponse> delete(Long id) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

		taskRepository.delete(task);

		BaseResponse<DataMap, TaskOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(task));
		return response;
	}

	private TaskOperationResponse mapToDto(Task task) {
		List<Long> labelIds = null;
		if (task.getLabels() != null) {
			labelIds = task.getLabels().stream().map(Label::getId).collect(Collectors.toList());
		}

		return new TaskOperationResponse(task.getId(), task.getProject() != null ? task.getProject().getId() : null,
				task.getSprint() != null ? task.getSprint().getId() : null,
				task.getTaskType() != null ? task.getTaskType().getId() : null,
				task.getParentTask() != null ? task.getParentTask().getId() : null, task.getTaskLevel(),
				task.getRankOrder(), task.getAssignee() != null ? task.getAssignee().getId() : null, task.getTitle(),
				task.getPriority(), task.getStatus(), task.getDueDate(), labelIds, task.getCreatedOn(),
				task.getUpdatedOn());
	}

	@Transactional
	public BaseResponse<DataMap, TaskOperationResponse> update(String status, Long id) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

		Long workflowId = task.getTaskType().getWorkflow().getId();

		WorkflowState workflowState = workflowStateRepository.findByWorkflowIdAndName(workflowId, task.getStatus())
				.get(0);

		List<WorkflowTransition> workflowTransaitions = workflowTransitionRepository
				.findByWorkflowIdAndFromState(workflowId, workflowState);

		List<String> endStatus = workflowTransaitions.stream()
				.map(workflowTransaition -> workflowTransaition.getToState()).map(state -> state.getName()).toList();

		if (!endStatus.contains(status)) {
			throw new InvalidWorkflowTransitionException("Transition not allowed");
		}
		task.setStatus(status);

		task = taskRepository.save(task);

		BaseResponse<DataMap, TaskOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(task));
		return response;
	}
}
