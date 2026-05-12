package com.velocity_flow.api.security.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.velocity_flow.api.entity.User;

/**
 * Custom implementation of Spring Security's UserDetails interface.
 */
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final String email;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;
	private final String status;

	public CustomUserDetails(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.status = user.getStatus();

		// Map permissions to GrantedAuthority
		if (user.getRole() != null && user.getRole().getPermissions() != null) {
			this.authorities = user.getRole().getPermissions().stream()
					.map(permission -> new SimpleGrantedAuthority(permission.getPermissionKey()))
					.collect(Collectors.toList());
		} else {
			this.authorities = List.of();
		}
	}

	public Long getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // Implement logic if needed based on entity properties
	}

	@Override
	public boolean isAccountNonLocked() {
		return "ACTIVE".equalsIgnoreCase(status); // Example locking logic
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return "ACTIVE".equalsIgnoreCase(status);
	}
}
