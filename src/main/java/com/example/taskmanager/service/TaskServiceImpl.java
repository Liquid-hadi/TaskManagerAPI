package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.exceptions.CustomException;
import com.example.taskmanager.repository.TaskRepo;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{
    private TaskRepo repo;
    public TaskServiceImpl(TaskRepo repo){this.repo = repo;}

    public TaskResponse create(CreateTask task){
        if (task == null){
            throw new HttpMessageNotReadableException("Request Body is empty!");
        }

        boolean hasName = task.getName() != null && !task.getName().trim().isEmpty();

        if(!hasName){
            throw new CustomException("Name is required!");
        }
        TaskEntity e = new TaskEntity(task.getName(), task.getDescription());
        if (task.getStatus() != null) e.setStatus(task.getStatus());
        if (task.getPriority() != null) e.setPriority(task.getPriority());
        TaskEntity saved = repo.save(e);

        return new TaskResponse(saved.getId(),
                                saved.getName(),
                                saved.getDescription(),
                                saved.getPriority(),
                                saved.getStatus(),
                                saved.getCreatedAt(),
                                saved.getUpdatedAt());
    }
    public List<TaskResponse> showTasks(){
        List<TaskEntity> tasks = repo.findAll();
        return tasks.stream().map(task ->new TaskResponse(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        )).toList();
    }
    public TaskResponse delete(Long Id) {
        repo.deleteById(Id);
        return null;
    }
    public  TaskResponse getById(Long Id){
        TaskEntity task = repo.findById(Id).orElseThrow(()-> new RuntimeException("Task not Found"));
        return new TaskResponse(task.getId(),
                                task.getName(),
                                task.getDescription(),
                                task.getPriority(),
                                task.getStatus(),
                                task.getCreatedAt(),
                                task.getUpdatedAt());

    }
    public TaskResponse updateTask(Long Id, TaskUpdate update){
        if (update == null){
            throw new HttpMessageNotReadableException("Request Body is empty!");
        }
        boolean hasName = update.getName() != null && !update.getName().trim().isEmpty();
        boolean hasDesc = update.getDescription() != null && !update.getDescription().trim().isEmpty();
        boolean hasStatus = update.getStatus().toString() != null && !update.getStatus().toString().isEmpty();
        boolean hasPriority = update.getPriority().toString() != null && !update.getPriority().toString().isEmpty();

        if(!hasName && !hasDesc && !hasPriority && hasStatus){
            throw new CustomException("At least 1 field should be updated");
        }

        TaskEntity task = repo.findById(Id).orElseThrow(()-> new CustomException("Task not found"));

        if(hasName) task.setName(update.getName());
        if(hasDesc) task.setDescription(update.getDescription());
        if(hasStatus) task.setStatus(update.getStatus());
        if(hasPriority) task.setPriority(update.getPriority());
        TaskEntity saved = repo.save(task);
        return new TaskResponse(saved.getId(),
                                saved.getName(),
                                saved.getDescription(),
                                saved.getPriority(),
                                saved.getStatus(),
                                saved.getCreatedAt(),
                                saved.getUpdatedAt());
    }
}
