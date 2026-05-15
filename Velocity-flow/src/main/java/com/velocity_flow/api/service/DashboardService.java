package com.velocity_flow.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.velocity_flow.api.dto.response.TaskOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Label;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.entity.Task;
import com.velocity_flow.api.entity.User;
import com.velocity_flow.api.entity.UserType;
import com.velocity_flow.api.entity.Workflow;
import com.velocity_flow.api.entity.WorkflowState;
import com.velocity_flow.api.repository.ProjectRepository;
import com.velocity_flow.api.repository.TaskRepository;
import com.velocity_flow.api.repository.UserRepository;
import com.velocity_flow.api.repository.WorkflowRepository;
import com.velocity_flow.api.repository.WorkflowStateRepository;
import com.velocity_flow.api.security.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final ProjectRepository projectRepository;
	private final WorkflowRepository workflowRepository;
	private final TaskRepository taskRepository;
	private final WorkflowStateRepository workflowStateRepository;
	private final UserRepository userRepository;

	public BaseResponse<DataMap, TaskOperationResponse> getDashboardDetails() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		String userRole = userDetails.getRole();

		List<Long> userIds = new ArrayList<>();
		Long organizationId = userDetails.getorganizationId();

		if (UserType.GLOBAL_ADMIN.name().equalsIgnoreCase(userRole)) {
			userIds = userRepository.findAll().stream().map(User::getId).toList();
		} else {
			userIds = userRepository.findByOrganizationId(organizationId).stream().map(User::getId).toList();
		}

		List<Project> projects = projectRepository.findByOrganizationId(organizationId);

		Set<String> workFlows = new HashSet<>();
		List<Task> tasks = new ArrayList<>();

		List<Long> projectIds = projects.stream().map(Project::getId).toList();

		tasks.addAll(taskRepository.findByProjectIdInAndAssigneeIdIn(projectIds, userIds));

		List<Workflow> workflows = workflowRepository.findByProjectIdIn(projectIds);

		List<Long> workflowIds = workflows.stream().map(Workflow::getId).toList();

		List<WorkflowState> workflowStates = workflowStateRepository.findByWorkflowIdIn(workflowIds);

		workflowStates.stream().map(WorkflowState::getName).forEach(workFlows::add);

		DataMap dataMap = new DataMap();
		dataMap.setKeysToShow(workFlows);

		List<TaskOperationResponse> dataList = tasks.stream().map(this::mapToDto).toList();
		BaseResponse<DataMap, TaskOperationResponse> response = new BaseResponse<DataMap, TaskOperationResponse>();
		response.setDataList(dataList);
		response.setDataMap(dataMap);

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
				task.getPriority(), task.getStatus(), task.getDueDate(), labelIds, task.getCreatedOn(), task.getUpdatedOn());
	}
}
