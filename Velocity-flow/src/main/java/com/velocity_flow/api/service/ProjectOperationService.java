package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.ProjectCreateRequest;
import com.velocity_flow.api.dto.request.ProjectUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Organization;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.repository.OrganizationRepository;
import com.velocity_flow.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectOperationService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public BaseResponse<DataMap, ProjectOperationResponse> create(ProjectCreateRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setSlug(request.getSlug());
        project.setStatus(request.getStatus());
        project.setVisibility(request.getVisibility());
        project.setCreatedAt(LocalDateTime.now());

        if (request.getOrganizationId() != null) {
            Organization org = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            project.setOrganization(org);
        }

        project = projectRepository.save(project);

        BaseResponse<DataMap, ProjectOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(project));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, ProjectOperationResponse> update(ProjectUpdateRequest request) {
        Project project = projectRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (request.getName() != null) project.setName(request.getName());
        if (request.getSlug() != null) project.setSlug(request.getSlug());
        if (request.getStatus() != null) project.setStatus(request.getStatus());
        if (request.getVisibility() != null) project.setVisibility(request.getVisibility());

        if (request.getOrganizationId() != null) {
            Organization org = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            project.setOrganization(org);
        }

        project = projectRepository.save(project);

        BaseResponse<DataMap, ProjectOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(project));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, ProjectOperationResponse> get(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        BaseResponse<DataMap, ProjectOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(project));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, ProjectOperationResponse> getAll() {
        BaseResponse<DataMap, ProjectOperationResponse> response = new BaseResponse<>(new DataMap());
        projectRepository.findAll().forEach(project -> response.getDataList().add(mapToDto(project)));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, ProjectOperationResponse> delete(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projectRepository.delete(project);

        BaseResponse<DataMap, ProjectOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(project));
        return response;
    }

    private ProjectOperationResponse mapToDto(Project project) {
        return new ProjectOperationResponse(
                project.getId(),
                project.getOrganization() != null ? project.getOrganization().getId() : null,
                project.getName(),
                project.getSlug(),
                project.getStatus(),
                project.getVisibility(),
                project.getCreatedAt(),
                project.getCreatedOn(),
                project.getUpdatedOn()
        );
    }
}
