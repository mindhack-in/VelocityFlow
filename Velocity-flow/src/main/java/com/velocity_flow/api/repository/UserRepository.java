package com.velocity_flow.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.User;
import com.velocity_flow.api.entity.UserType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	List<User> findByUserType(UserType userType);

	List<User> findByOrganizationId(Long organizationId);

}
