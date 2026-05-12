package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "WORKFLOW_STATES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowState extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    private String name;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Column(name = "is_initial")
    private Boolean isInitial;

    @Column(name = "is_final")
    private Boolean isFinal;
}
