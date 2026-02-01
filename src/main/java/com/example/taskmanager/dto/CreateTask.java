package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTask {
    @Schema(example = "Buy Groceries")
    private String name;

    @Schema(example = "1.Milk 2.Eggs")
    private String Description;

    @Schema(example = "Todo")
    private TaskStatus status;

    @Schema(example = "Medium")
    private TaskPriority priority;

}
