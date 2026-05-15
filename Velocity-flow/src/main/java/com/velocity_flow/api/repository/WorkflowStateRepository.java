package com.velocity_flow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.WorkflowState;

@Repository
public interface WorkflowStateRepository extends JpaRepository<WorkflowState, Long> {
	List<WorkflowState> findByWorkflowIdIn(List<Long> workflowIds);

	List<WorkflowState> findByWorkflowIdAndName(Long id,String name);

}
