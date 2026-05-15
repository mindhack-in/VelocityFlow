package com.velocity_flow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.Sprint;
import com.velocity_flow.api.entity.TaskType;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

	List<TaskType> findByProjectIdIn(List<Long> projectIds);

}
