package com.example.taskmanager.controller;


import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    /* This constructor is to map the service to the controller, we can
       use @Automapping, but it's not recommended  */
    public TaskController(TaskService service){this.service=service;}

    @Operation(summary = "Create new Task")
    @PostMapping
    public TaskResponse create(@Valid @RequestBody CreateTask task){
        return service.create(task);
    }

    @Operation(summary = "Show tasks")
    @GetMapping
    public List<TaskResponse> showTasks(
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateTo
            ){
        return service.showTasks(priority, status, q, dueDateFrom, dueDateTo);
    }




    @Operation(summary = "Get task by ID number")
    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @Operation(summary = "Update tasks")
    @PutMapping ("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,@Valid @RequestBody TaskUpdate task){
        return service.updateTask(id, task);
    }

    @Operation(summary = "Delete task by ID number")
    @DeleteMapping ("/{Id}")
    public TaskResponse delete(@PathVariable Long Id) throws Exception {return service.delete(Id);}


}
