package com.velocity_flow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.WorkflowState;
import com.velocity_flow.api.entity.WorkflowTransition;

@Repository
public interface WorkflowTransitionRepository extends JpaRepository<WorkflowTransition, Long> {

	List<WorkflowTransition> findByWorkflowIdAndFromState(Long workflowId, WorkflowState fromState);

}
