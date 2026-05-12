package com.velocity_flow.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@PrePersist
	protected void onCreate() {
		this.createdOn = LocalDateTime.now();
		this.updatedOn = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedOn = LocalDateTime.now();
	}
}
