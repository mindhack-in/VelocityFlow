package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.ProjectRoleCreateRequest;
import com.velocity_flow.api.dto.request.ProjectRoleUpdateRequest;
import com.velocity_flow.api.dto.response.ProjectRoleOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.ProjectRole;
import com.velocity_flow.api.repository.ProjectRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectRoleOperationService {

    private final ProjectRoleRepository projectRoleRepository;

    @Transactional
    public BaseResponse<DataMap, ProjectRoleOperationResponse> create(ProjectRoleCreateRequest request) {
        ProjectRole role = new ProjectRole();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        role = projectRoleRepository.save(role);

        BaseResponse<DataMap, ProjectRoleOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(role));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, ProjectRoleOperationResponse> update(ProjectRoleUpdateRequest request) {
        ProjectRole role = projectRoleRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("ProjectRole not found"));

        if (request.getName() != null) role.setName(request.getName());
        if (request.getDescription() != null) role.setDescription(request.getDescription());

        role = projectRoleRepository.save(role);

        BaseResponse<DataMap, ProjectRoleOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(role));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> get(Long id) {
        ProjectRole role = projectRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectRole not found"));

        BaseResponse<DataMap, ProjectRoleOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(role));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, ProjectRoleOperationResponse> getAll() {
        BaseResponse<DataMap, ProjectRoleOperationResponse> response = new BaseResponse<>(new DataMap());
        projectRoleRepository.findAll().forEach(role -> response.getDataList().add(mapToDto(role)));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, ProjectRoleOperationResponse> delete(Long id) {
        ProjectRole role = projectRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProjectRole not found"));

        projectRoleRepository.delete(role);

        BaseResponse<DataMap, ProjectRoleOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(role));
        return response;
    }

    private ProjectRoleOperationResponse mapToDto(ProjectRole role) {
        return new ProjectRoleOperationResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCreatedOn(),
                role.getUpdatedOn()
        );
    }
}
