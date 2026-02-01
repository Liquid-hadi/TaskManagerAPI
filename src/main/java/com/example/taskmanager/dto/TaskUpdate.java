package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdate {
    @Schema(example = "Develop software")
    private String name;
    @Schema(example = "Use OpenAPI for Documentation")
    private String Description;
    @Schema(example = "Todo")
    private TaskStatus status;
    @Schema(example = "Priority")
    private TaskPriority priority;

}
