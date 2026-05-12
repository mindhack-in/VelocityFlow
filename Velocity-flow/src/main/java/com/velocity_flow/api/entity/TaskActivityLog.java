package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TASK_ACTIVITY_LOGS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskActivityLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;
}
