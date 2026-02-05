package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskUpdate {
    @Schema(example = "Develop software")
    private String name;

    @Schema(example = "Use OpenAPI for Documentation")
    private String Description;

    @Schema(example = "2026-02-15")
    private LocalDate dueDate;

    @Schema(example = "Todo")
    private TaskStatus status;

    @Schema(example = "Priority")
    private TaskPriority priority;

}
