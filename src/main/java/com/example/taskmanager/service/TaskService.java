package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;

import java.util.List;

public interface TaskService {
    TaskResponse create(CreateTask task);
    List<TaskResponse> showTasks();
    TaskResponse delete(Long Id);
    TaskResponse getById(Long Id);
    TaskResponse updateTask(Long Id, TaskUpdate update);
}
