package com.velocity_flow.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TASK_COMMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commented_by")
    private User commentedBy;

    private String comment;
}
