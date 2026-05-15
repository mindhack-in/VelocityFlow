package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.ProjectUserCreateRequest;
import com.velocity_flow.api.dto.request.ProjectUserUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectUserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.entity.ProjectRole;
import com.velocity_flow.api.entity.ProjectUser;
import com.velocity_flow.api.entity.User;
import com.velocity_flow.api.repository.ProjectRepository;
import com.velocity_flow.api.repository.ProjectRoleRepository;
import com.velocity_flow.api.repository.ProjectUserRepository;
import com.velocity_flow.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectUserOperationService {

    private final ProjectUserRepository projectUserRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectRoleRepository projectRoleRepository;

    @Transactional
    public BaseResponse<DataMap, ProjectUserOperationResponse> create(ProjectUserCreateRequest request) {
        ProjectUser projectUser = new ProjectUser();
        projectUser.setAssignedAt(LocalDateTime.now());

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            projectUser.setProject(project);
        }

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            projectUser.setUser(user);
        }

        if (request.getProjectRoleId() != null) {
            ProjectRole role = projectRoleRepository.findById(request.getProjectRoleId())
                    .orElseThrow(() -> new RuntimeException("ProjectRole not found"));
            projectUser.setProjectRole(role);
        }

        projectUser = projectUserRepository.save(projectUser);

        BaseResponse<DataMap, ProjectUserOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(projectUser));
        return response;
    }

    public BaseResponse<DataMap, ProjectUserOperationResponse> update(ProjectUserUpdateRequest request) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public BaseResponse<DataMap, ProjectUserOperationResponse> get(Long id) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public BaseResponse<DataMap, ProjectUserOperationResponse> getAll() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Transactional
    public BaseResponse<DataMap, ProjectUserOperationResponse> delete(Long id) {
        ProjectUser projectUser = projectUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectUser not found"));

        projectUserRepository.delete(projectUser);

        BaseResponse<DataMap, ProjectUserOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(projectUser));
        return response;
    }

    private ProjectUserOperationResponse mapToDto(ProjectUser projectUser) {
        return new ProjectUserOperationResponse(
                projectUser.getId(),
                projectUser.getProject() != null ? projectUser.getProject().getId() : null,
                projectUser.getUser() != null ? projectUser.getUser().getId() : null,
                projectUser.getProjectRole() != null ? projectUser.getProjectRole().getId() : null,
                projectUser.getAssignedAt(),
                projectUser.getCreatedOn(),
                projectUser.getUpdatedOn()
        );
    }
}
