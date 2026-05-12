package com.velocity_flow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
