package com.velocity_flow.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WORKFLOW_TRANSITIONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTransition extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_id")
	private Workflow workflow;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_state_id")
	private WorkflowState fromState;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_state_id")
	private WorkflowState toState;
}
