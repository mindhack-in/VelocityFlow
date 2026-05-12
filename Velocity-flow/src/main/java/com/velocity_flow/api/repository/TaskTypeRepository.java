package com.velocity_flow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.TaskType;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {
}
