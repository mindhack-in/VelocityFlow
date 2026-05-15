package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.LabelCreateRequest;
import com.velocity_flow.api.dto.request.LabelUpdateRequest;
import com.velocity_flow.api.dto.response.LabelOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Label;
import com.velocity_flow.api.entity.Project;
import com.velocity_flow.api.repository.LabelRepository;
import com.velocity_flow.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabelOperationService {

    private final LabelRepository labelRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public BaseResponse<DataMap, LabelOperationResponse> create(LabelCreateRequest request) {
        Label label = new Label();
        label.setName(request.getName());
        label.setColor(request.getColor());

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            label.setProject(project);
        }

        label = labelRepository.save(label);

        BaseResponse<DataMap, LabelOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(label));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, LabelOperationResponse> update(LabelUpdateRequest request) {
        Label label = labelRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Label not found"));

        if (request.getName() != null) label.setName(request.getName());
        if (request.getColor() != null) label.setColor(request.getColor());
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            label.setProject(project);
        }

        label = labelRepository.save(label);

        BaseResponse<DataMap, LabelOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(label));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, LabelOperationResponse> get(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        BaseResponse<DataMap, LabelOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(label));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, LabelOperationResponse> getAll() {
        BaseResponse<DataMap, LabelOperationResponse> response = new BaseResponse<>(new DataMap());
        List<Label> labels = labelRepository.findAll();
        response.setDataList(labels.stream().map(this::mapToDto).collect(Collectors.toList()));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, LabelOperationResponse> delete(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        labelRepository.delete(label);

        BaseResponse<DataMap, LabelOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(label));
        return response;
    }

    private LabelOperationResponse mapToDto(Label label) {
        return new LabelOperationResponse(
                label.getId(),
                label.getProject() != null ? label.getProject().getId() : null,
                label.getName(),
                label.getColor(),
                label.getCreatedOn(),
                label.getUpdatedOn()
        );
    }
}
