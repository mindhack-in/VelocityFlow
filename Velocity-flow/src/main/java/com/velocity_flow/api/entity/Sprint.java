package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SPRINTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sprint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String name;

    @Column(name = "capacity_hours")
    private Integer capacityHours;

    @Column(name = "planned_story_points")
    private Integer plannedStoryPoints;

    @Column(name = "completed_story_points")
    private Integer completedStoryPoints;
}
