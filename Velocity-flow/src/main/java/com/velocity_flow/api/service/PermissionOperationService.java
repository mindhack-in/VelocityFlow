package com.velocity_flow.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.response.PermissionOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Permission;
import com.velocity_flow.api.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionOperationService {

	private final PermissionRepository permissionRepository;

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, PermissionOperationResponse> get(Long id) {
		Permission entity = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		BaseResponse<DataMap, PermissionOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(entity));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, PermissionOperationResponse> getAll() {
		BaseResponse<DataMap, PermissionOperationResponse> response = new BaseResponse<>(new DataMap());
		permissionRepository.findAll().forEach(p -> response.getDataList().add(mapToDto(p)));
		return response;
	}

	private PermissionOperationResponse mapToDto(Permission p) {
		return new PermissionOperationResponse(p.getId(), p.getPermissionKey(), p.getDescription(), p.getCreatedOn(),
				p.getUpdatedOn());
	}
}
