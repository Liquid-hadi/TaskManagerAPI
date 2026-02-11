package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface TaskService {
    TaskResponse create(CreateTask task);
    Page<TaskResponse> showTasks(TaskPriority priority, TaskStatus status, String q, LocalDate dueDateFrom, LocalDate dueDateTo, int page, int size, String sortBy, String sortDir);
    TaskResponse delete(Long Id);
    TaskResponse getById(Long Id);
    TaskResponse updateTask(Long Id, TaskUpdate update);
}
