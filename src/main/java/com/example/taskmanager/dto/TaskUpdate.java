package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskUpdate {
    @Schema(example = "Develop software")
    @NotBlank
    private String name;
    private String Description;
}
