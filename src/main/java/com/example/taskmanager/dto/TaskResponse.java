package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskResponse {
    @Schema(example = "1")
    private Long Id;

    @Schema(example = "Buy Groceries")
    private String name;

    @Schema(example = "1.milk 2.eggs")
    private String Description;

    public TaskResponse(){}

    public TaskResponse(Long Id, String name, String Description){
        this.Description = Description;
        this.Id=Id;
        this.name = name;
    }
}
