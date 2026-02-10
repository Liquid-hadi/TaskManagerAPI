package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CreateTask {
    @Schema(example = "Buy Groceries")
    private String name;

    @Schema(example = "1.Milk 2.Eggs")
    private String Description;

    @Schema(example = "2026-02-15")
    private LocalDate dueDate;

    @Schema(example = "Todo")
    private TaskStatus status;

    @Schema(example = "Medium")
    private TaskPriority priority;

}
