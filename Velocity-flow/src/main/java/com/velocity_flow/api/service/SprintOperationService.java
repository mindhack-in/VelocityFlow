package com.velocity_flow.api.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.request.SprintCreateRequest;
import com.velocity_flow.api.dto.request.SprintUpdateRequest;
import com.velocity_flow.api.dto.response.SprintOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.entity.Sprint;
import com.velocity_flow.api.repository.ProjectRepository;
import com.velocity_flow.api.repository.SprintRepository;
import com.velocity_flow.api.repository.WorkflowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SprintOperationService {

	private final SprintRepository sprintRepository;
	private final ProjectRepository projectRepository;
	private final WorkflowRepository workflowRepository;

	@Transactional
	public BaseResponse<DataMap, SprintOperationResponse> create(SprintCreateRequest request) {
		Sprint sprint = new Sprint();
		sprint.setName(request.getName());
		sprint.setCapacityHours(request.getCapacityHours());
		sprint.setPlannedStoryPoints(request.getPlannedStoryPoints());
		sprint.setCompletedStoryPoints(request.getCompletedStoryPoints());

		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new RuntimeException("Project not found"));
			sprint.setProject(project);
		}

		sprint = sprintRepository.save(sprint);

		BaseResponse<DataMap, SprintOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(sprint));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, SprintOperationResponse> update(SprintUpdateRequest request) {
		Sprint sprint = sprintRepository.findById(request.getId())
				.orElseThrow(() -> new RuntimeException("Sprint not found"));

		if (request.getName() != null)
			sprint.setName(request.getName());
		if (request.getCapacityHours() != null)
			sprint.setCapacityHours(request.getCapacityHours());
		if (request.getPlannedStoryPoints() != null)
			sprint.setPlannedStoryPoints(request.getPlannedStoryPoints());
		if (request.getCompletedStoryPoints() != null)
			sprint.setCompletedStoryPoints(request.getCompletedStoryPoints());

		if (request.getProjectId() != null) {
			Project project = projectRepository.findById(request.getProjectId())
					.orElseThrow(() -> new RuntimeException("Project not found"));
			sprint.setProject(project);
		}

		sprint = sprintRepository.save(sprint);

		BaseResponse<DataMap, SprintOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(sprint));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, SprintOperationResponse> get(Long id) {
		Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new RuntimeException("Sprint not found"));

		BaseResponse<DataMap, SprintOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(sprint));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, SprintOperationResponse> getAll() {
		BaseResponse<DataMap, SprintOperationResponse> response = new BaseResponse<>(new DataMap());
		sprintRepository.findAll().forEach(sprint -> response.getDataList().add(mapToDto(sprint)));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, SprintOperationResponse> delete(Long id) {
		Sprint sprint = sprintRepository.findById(id).orElseThrow(() -> new RuntimeException("Sprint not found"));

		sprintRepository.delete(sprint);

		BaseResponse<DataMap, SprintOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(sprint));
		return response;
	}

	private SprintOperationResponse mapToDto(Sprint sprint) {
		return new SprintOperationResponse(sprint.getId(),
				sprint.getProject() != null ? sprint.getProject().getId() : null, sprint.getName(),
				sprint.getCapacityHours(), sprint.getPlannedStoryPoints(), sprint.getCompletedStoryPoints(),
				sprint.getCreatedOn(), sprint.getUpdatedOn());
	}

	public BaseResponse<DataMap, SprintOperationResponse> getByProjectId(Long projectId) {
		List<Sprint> sprint = sprintRepository.findByProjectIdIn(Arrays.asList(projectId));

		BaseResponse<DataMap, SprintOperationResponse> response = new BaseResponse<>(new DataMap());

		sprint.forEach(x -> response.getDataList().add(mapToDto(x)));

		return response;
	}
}
