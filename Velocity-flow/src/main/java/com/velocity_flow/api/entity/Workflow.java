package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "WORKFLOWS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workflow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "workflow_type")
    private String workflowType;

    private String name;

    private Boolean reusable;
}
