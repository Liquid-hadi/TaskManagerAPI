package com.example.taskmanager.entity;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Tasks")

public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(nullable = false)
    private LocalDate dueDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant CreatedAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant UpdatedAt;

    public TaskEntity(){}

    public TaskEntity(String name, String Description, LocalDate dueDate){
        this.name=name;
        this.description =Description;
        this.dueDate = dueDate;
    }

}
