package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskResponse create(CreateTask task);
    List<TaskResponse> showTasks(TaskPriority priority, TaskStatus status, String q, LocalDate dueDateFrom, LocalDate dueDateTo);
    TaskResponse delete(Long Id);
    TaskResponse getById(Long Id);
    TaskResponse updateTask(Long Id, TaskUpdate update);
}
