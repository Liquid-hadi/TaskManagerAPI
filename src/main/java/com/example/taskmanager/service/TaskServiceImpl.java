package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exceptions.CustomException;
import com.example.taskmanager.repository.TaskRepo;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        boolean hasDueDate = task.getDueDate() != null;
        LocalDate today = LocalDate.now();

        if(!hasName || !hasDueDate){
            throw new CustomException("please fill all required fields!");
        }
        if(task.getDueDate().isBefore(today)){
            throw new CustomException("Date should not be in the past!");
        }
        TaskEntity e = new TaskEntity(task.getName(), task.getDescription(), task.getDueDate());
        if (task.getStatus() != null) e.setStatus(task.getStatus());
        if (task.getPriority() != null) e.setPriority(task.getPriority());
        TaskEntity saved = repo.save(e);

        return new TaskResponse(saved.getId(),
                                saved.getName(),
                                saved.getDescription(),
                                saved.getDueDate(),
                                saved.getPriority(),
                                saved.getStatus(),
                                saved.getCreatedAt(),
                                saved.getUpdatedAt());
    }
    public List<TaskResponse> showTasks(){
        List<TaskEntity> tasks = repo.findAll();

        LocalDate today = LocalDate.now();

        for(TaskEntity task : tasks){
            if (task.getStatus() == TaskStatus.IN_PROGRESS && task.getDueDate().isBefore(today))
            {
                task.setPriority(TaskPriority.HIGH);
            }
        }

        return tasks.stream().map(task ->new TaskResponse(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getDueDate(),
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
                                task.getDueDate(),
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
        boolean hasDueDate = update.getDueDate() != null;
        LocalDate today = LocalDate.now();



        if(update.getDueDate().isBefore(today)){
            throw new CustomException("Date should not be in the past!");
        }

        if(!hasName && !hasDesc && !hasPriority && !hasStatus && !hasDueDate){
            throw new CustomException("At least 1 field should be updated");
        }

        TaskEntity task = repo.findById(Id).orElseThrow(()-> new CustomException("Task not found"));

        if(!task.getDueDate().equals(update.getDueDate()) && update.getStatus().equals(TaskStatus.IN_PROGRESS)){
                throw new CustomException("Cant change due date while status is IN_PROGRESS");
            }
        if((!task.getName().equals(update.getName()) || !task.getDescription().equals(update.getDescription())
                || !task.getPriority().equals(update.getPriority()))
                && update.getStatus().equals(TaskStatus.DONE))
        {
            throw new CustomException("Cant change edit task while status is DONE");
        }
        if (task.getStatus() == TaskStatus.TODO && update.getStatus() == TaskStatus.DONE)
        {
            throw new CustomException("Task status must be IN_PROGRESS before its DONE");
        }

        if(hasName) task.setName(update.getName());
        if(hasDesc) task.setDescription(update.getDescription());
        if(hasDueDate) task.setDueDate(update.getDueDate());
        if(hasStatus) task.setStatus(update.getStatus());
        if(hasPriority) task.setPriority(update.getPriority());
        TaskEntity saved = repo.save(task);
        return new TaskResponse(saved.getId(),
                                saved.getName(),
                                saved.getDescription(),
                                saved.getDueDate(),
                                saved.getPriority(),
                                saved.getStatus(),
                                saved.getCreatedAt(),
                                saved.getUpdatedAt());
    }
}
