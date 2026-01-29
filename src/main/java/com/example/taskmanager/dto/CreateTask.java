package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTask {
    @Schema(example = "Buy Groceries")
    @NotBlank
    private String name;

    @Schema(example = "1.Milk 2.Eggs")
    private String Description;
}
