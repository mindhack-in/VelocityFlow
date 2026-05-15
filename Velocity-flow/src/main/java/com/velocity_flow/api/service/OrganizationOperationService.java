package com.velocity_flow.api.service;

import com.velocity_flow.api.dto.request.OrganizationCreateRequest;
import com.velocity_flow.api.dto.request.OrganizationUpdateRequest;
import com.velocity_flow.api.dto.response.OrganizationOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Organization;
import com.velocity_flow.api.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationOperationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public BaseResponse<DataMap, OrganizationOperationResponse> create(OrganizationCreateRequest request) {
        Organization org = new Organization();
        org.setName(request.getName());
        org.setSlug(request.getSlug());
        
        org = organizationRepository.save(org);
        
        BaseResponse<DataMap, OrganizationOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(org));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, OrganizationOperationResponse> update(OrganizationUpdateRequest request) {
        Organization org = organizationRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
                
        if (request.getName() != null) org.setName(request.getName());
        if (request.getSlug() != null) org.setSlug(request.getSlug());
        
        org = organizationRepository.save(org);
        
        BaseResponse<DataMap, OrganizationOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(org));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, OrganizationOperationResponse> get(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
                
        BaseResponse<DataMap, OrganizationOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(org));
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponse<DataMap, OrganizationOperationResponse> getAll() {
        BaseResponse<DataMap, OrganizationOperationResponse> response = new BaseResponse<>(new DataMap());
        organizationRepository.findAll().forEach(org -> response.getDataList().add(mapToDto(org)));
        return response;
    }

    @Transactional
    public BaseResponse<DataMap, OrganizationOperationResponse> delete(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
                
        organizationRepository.delete(org);
        
        BaseResponse<DataMap, OrganizationOperationResponse> response = new BaseResponse<>(new DataMap());
        response.getDataList().add(mapToDto(org));
        return response;
    }

    private OrganizationOperationResponse mapToDto(Organization org) {
        return new OrganizationOperationResponse(
                org.getId(),
                org.getName(),
                org.getSlug(),
                org.getCreatedOn(),
                org.getUpdatedOn()
        );
    }
}
