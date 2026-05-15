package com.velocity_flow.api.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.velocity_flow.api.dto.request.UserCreateRequest;
import com.velocity_flow.api.dto.request.UserUpdateRequest;
import com.velocity_flow.api.dto.response.UserOperationResponse;
import com.velocity_flow.api.dto.response.base.BaseResponse;
import com.velocity_flow.api.dto.response.base.DataMap;
import com.velocity_flow.api.entity.Organization;
import com.velocity_flow.api.entity.Role;
import com.velocity_flow.api.entity.User;
import com.velocity_flow.api.entity.UserType;
import com.velocity_flow.api.repository.OrganizationRepository;
import com.velocity_flow.api.repository.RoleRepository;
import com.velocity_flow.api.repository.UserRepository;
import com.velocity_flow.api.security.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserOperationService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final OrganizationRepository organizationRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public BaseResponse<DataMap, UserOperationResponse> create(UserCreateRequest request, UserType userType) {

		BaseResponse<DataMap, UserOperationResponse> response = new BaseResponse<DataMap, UserOperationResponse>();
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setStatus("ACTIVE");
		user.setUserType(userType);

		if (request.getRoleId() != null) {
			Role role = roleRepository.findById(request.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role not found"));
			user.setRole(role);
		}

		if (request.getOrganizationId() != null) {
			Organization org = organizationRepository.findById(request.getOrganizationId())
					.orElseThrow(() -> new RuntimeException("Organization not found"));
			user.setOrganization(org);
		}

		user = userRepository.save(user);
		UserOperationResponse dataList = mapToResponse(user);

		response.setDataList(Arrays.asList(dataList));

		return response;
	}

	@Transactional
	public BaseResponse<DataMap, UserOperationResponse> update(UserUpdateRequest request) {

		BaseResponse<DataMap, UserOperationResponse> response = new BaseResponse<DataMap, UserOperationResponse>();

		User user = userRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("User not found"));

		if (request.getName() != null)
			user.setName(request.getName());
		if (request.getEmail() != null)
			user.setEmail(request.getEmail());
		if (request.getStatus() != null)
			user.setStatus(request.getStatus());

		if (request.getRoleId() != null) {
			Role role = roleRepository.findById(request.getRoleId())
					.orElseThrow(() -> new RuntimeException("Role not found"));
			user.setRole(role);
		}

		if (request.getOrganizationId() != null) {
			Organization org = organizationRepository.findById(request.getOrganizationId())
					.orElseThrow(() -> new RuntimeException("Organization not found"));
			user.setOrganization(org);
		}

		user = userRepository.save(user);
		UserOperationResponse dataList = mapToResponse(user);

		response.setDataList(Arrays.asList(dataList));

		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, UserOperationResponse> get(Long id) {

		BaseResponse<DataMap, UserOperationResponse> response = new BaseResponse<DataMap, UserOperationResponse>();

		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		UserOperationResponse dataList = mapToResponse(user);

		response.setDataList(Arrays.asList(dataList));

		return response;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, UserOperationResponse> getAll(UserType userType) {
		BaseResponse<DataMap, UserOperationResponse> response = new BaseResponse<DataMap, UserOperationResponse>();
		List<User> user = userRepository.findByUserType(userType);

		response.setDataList(user.stream().map(this::mapToResponse).collect(Collectors.toList()));
		return response;
	}

	@Transactional
	public BaseResponse<DataMap, UserOperationResponse> delete(Long id) {
		BaseResponse<DataMap, UserOperationResponse> response = new BaseResponse<DataMap, UserOperationResponse>();

		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		userRepository.delete(user);
		UserOperationResponse dataList = mapToResponse(user);

		response.setDataList(Arrays.asList(dataList));

		return response;
	}

	private UserOperationResponse mapToResponse(User user) {

		UserOperationResponse dataList = new UserOperationResponse(user.getId(), user.getName(), user.getEmail(),
				user.getStatus(), user.getRole() != null ? user.getRole().getId() : null,
				user.getOrganization() != null ? user.getOrganization().getId() : null, user.getCreatedOn(),
				user.getUpdatedOn());

		return dataList;
	}

	@Transactional(readOnly = true)
	public BaseResponse<DataMap, UserOperationResponse> getByOrganization() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		Long organizationId = userDetails.getorganizationId();

		BaseResponse<DataMap, UserOperationResponse> response = new BaseResponse<DataMap, UserOperationResponse>();
		List<User> user = userRepository.findByOrganizationId(organizationId);

		response.setDataList(user.stream().map(this::mapToResponse).collect(Collectors.toList()));
		return response;
	}
}
