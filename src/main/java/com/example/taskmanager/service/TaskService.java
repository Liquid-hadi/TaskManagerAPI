package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private TaskRepo repo;
    public TaskService(TaskRepo repo){this.repo = repo;}

    public TaskResponse create(CreateTask task){
        TaskEntity saved = repo.save(new TaskEntity(task.getName(), task.getDescription()));
        return new TaskResponse(saved.getId(),saved.getName(),saved.getDescription());
    }
    public List<TaskResponse> showTasks(){
        List<TaskEntity> tasks = repo.findAll();
        return tasks.stream().map(task ->new TaskResponse(
                task.getId(),
                task.getName(),
                task.getDescription()
        )).toList();
    }
    public TaskResponse delete(Long Id) throws Exception {
        if (!repo.existsById(Id)){
            throw new Exception("Task Not found with ID: "+Id);
        }
        repo.deleteById(Id);
        return null;
    }
    public  TaskResponse getById(Long Id){
        TaskEntity task = repo.findById(Id).orElseThrow(()-> new RuntimeException("Task not Found"));
        return new TaskResponse(task.getId(), task.getName(), task.getDescription());

    }
    public TaskResponse updateTask(Long Id, TaskUpdate update){
        TaskEntity task = repo.findById(Id).orElseThrow(()-> new RuntimeException("Task not found"));
        task.setName(update.getName());
        task.setDescription((update.getDescription()));
        TaskEntity saved = repo.save(task);
        return new TaskResponse(saved.getId(),saved.getName(), saved.getDescription());
    }
}
