package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TASK_STATE_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStateHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_state_id")
    private WorkflowState fromState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_state_id")
    private WorkflowState toState;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
