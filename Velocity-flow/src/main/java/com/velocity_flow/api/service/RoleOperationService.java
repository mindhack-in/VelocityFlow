package com.velocity_flow.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.request.RoleCreateRequest;
import com.velocity_flow.api.dto.request.RoleUpdateRequest;
import com.velocity_flow.api.dto.response.RoleOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Permission;
import com.velocity_flow.api.entity.Role;
import com.velocity_flow.api.repository.PermissionRepository;
import com.velocity_flow.api.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleOperationService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	@Transactional
	public BaseResponse<DataMap, RoleOperationResponse> create(RoleCreateRequest request) {
		Role role = new Role();
		role.setName(request.getName());

		if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
			List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
			role.setPermissions(new HashSet<>(permissions));
		}

		role = roleRepository.save(role);

		BaseResponse<DataMap, RoleOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(role));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, RoleOperationResponse> update(RoleUpdateRequest request) {
		Role role = roleRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Role not found"));

		if (request.getName() != null)
			role.setName(request.getName());

		if (request.getPermissionIds() != null) {
			List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
			role.setPermissions(new HashSet<>(permissions));
		}

		role = roleRepository.save(role);

		BaseResponse<DataMap, RoleOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(role));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, RoleOperationResponse> get(Long id) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		BaseResponse<DataMap, RoleOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(role));
		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, RoleOperationResponse> getAll() {
		BaseResponse<DataMap, RoleOperationResponse> response = new BaseResponse<>(new DataMap());
		roleRepository.findAll().forEach(role -> response.getDataList().add(mapToDto(role)));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, RoleOperationResponse> delete(Long id) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));

		roleRepository.delete(role);

		BaseResponse<DataMap, RoleOperationResponse> response = new BaseResponse<>(new DataMap());
		response.getDataList().add(mapToDto(role));
		return response;
	}

	private RoleOperationResponse mapToDto(Role role) {
		List<String> permissionIds = null;
		if (role.getPermissions() != null) {
			permissionIds = role.getPermissions().stream().map(Permission::getDescription).collect(Collectors.toList());
		}

		return new RoleOperationResponse(role.getId(), role.getName(), permissionIds, role.getCreatedOn(),
				role.getUpdatedOn());
	}
}
