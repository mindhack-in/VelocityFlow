package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TASK_TYPES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    private String name;

    private String icon;

    private String color;
}
