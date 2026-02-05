package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Setter
@Getter
public class TaskResponse {
    @Schema(example = "1")
    private Long Id;

    @Schema(example = "Buy Groceries")
    private String name;

    @Schema(example = "1.milk 2.eggs")
    private String Description;

    @Schema(example = "2026-02-15")
    private LocalDate dueDate;

    @Schema(example = "Todo")
    private TaskStatus status;

    @Schema(example = "Medium")
    private TaskPriority priority;

    private Instant createdAt;
    private Instant updatedAt;

    public TaskResponse(){}

    public TaskResponse(Long Id, String name, String Description, LocalDate dueDate,TaskPriority priority, TaskStatus status, Instant createdAt, Instant updatedAt){
        this.Description = Description;
        this.Id=Id;
        this.name = name;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
