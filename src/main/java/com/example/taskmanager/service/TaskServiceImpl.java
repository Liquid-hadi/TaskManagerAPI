package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTask;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskUpdate;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exceptions.CustomException;
import com.example.taskmanager.repository.TaskRepo;
import com.example.taskmanager.spec.TaskSpecifications;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskServiceImpl implements TaskService{
    private final TaskRepo repo;
    public TaskServiceImpl(TaskRepo repo){this.repo = repo;}

    //==============CREATE Function================
    public TaskResponse create(CreateTask task){
        TaskEntity e = new TaskEntity(task.getName(), task.getDescription(), task.getDueDate());
        validateCreate(task,e);
        return map(repo.save(e));
    }

    //===============Show All Tasks=================
    @Transactional
    public Page<TaskResponse> showTasks(TaskPriority priority, TaskStatus status, String q, LocalDate dueDateFrom, LocalDate dueDateTo, int page, int  size, String sortBy, String sortDir){

        if(!sortBy.equals("CreatedAt") && !sortBy.equals("dueDate") && !sortBy.equals("priority")) {
            sortBy = "CreatedAt";
        }

        Sort.Direction direction;

        if ("asc".equalsIgnoreCase(sortDir)) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(0,50, Sort.by(direction, sortBy));

        Specification<TaskEntity> spec = TaskSpecifications.notDeleted()
                .and(TaskSpecifications.hasPriority(priority))
                .and(TaskSpecifications.hasSatus(status))
                .and(TaskSpecifications.SearchQ(q))
                .and(TaskSpecifications.dueDateBetween(dueDateFrom,dueDateTo));

        Page<TaskEntity> tasks = repo.findAll(spec, pageable);

        for(TaskEntity task : tasks){
            if (task.getStatus() == TaskStatus.IN_PROGRESS && task.getDueDate().isBefore(LocalDate.now()))
            {
                task.setPriority(TaskPriority.HIGH);
            }
        }

        return tasks.map(task ->new TaskResponse(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        ));
    }
    // ==================DELETE FUNCTION==================
    @Transactional
    public TaskResponse delete(Long Id) {
        validateID(Id);
        int updated = repo.softDeleteById(Id);
        return null;
    }

    // =========== not yet added in frontend
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
        //=====================UPDATE Function====================
    public TaskResponse updateTask(Long Id, TaskUpdate update){
        TaskEntity task = repo.findById(Id).orElseThrow(()-> new CustomException("Task not found"));
        validateUpdate(Id , update, task);

        //=====================Business logic====================
        if(!task.getDueDate().equals(update.getDueDate()) && update.getStatus().equals(TaskStatus.IN_PROGRESS))
            throw new CustomException("Cant change due date while status is IN_PROGRESS");
        if((!task.getName().equals(update.getName()) || !task.getDescription().equals(update.getDescription()) ||
                !task.getPriority().equals(update.getPriority())) && update.getStatus().equals(TaskStatus.DONE))
            throw new CustomException("Cant change edit task while status is DONE");
        if (task.getStatus() == TaskStatus.TODO && update.getStatus() == TaskStatus.DONE)
            throw new CustomException("Task status must be IN_PROGRESS before its DONE");
        //=====================SAVE NEW TASK====================
        if (notBlank(update.getName())) task.setName(update.getName());
        if (notBlank(update.getDescription())) task.setDescription(update.getDescription());
        if (update.getDueDate() != null) task.setDueDate(update.getDueDate());
        if (update.getPriority() != null) task.setPriority(update.getPriority());
        if (update.getStatus() != null) task.setStatus(update.getStatus());
        return map(repo.save(task));
    }

                // validation and helpers
    private boolean isBlank(String s) {return s == null || s.isBlank();}
    private boolean notBlank(String s) {return s != null || !s.isBlank();}
    private TaskResponse map(TaskEntity e) {return new TaskResponse(e.getId(), e.getName(), e.getDescription(), e.getDueDate(), e.getPriority(),e.getStatus(),e.getCreatedAt(),e.getUpdatedAt());}
    private void validateCreate(CreateTask task, TaskEntity e){
        if (task == null){throw new CustomException("Request Body is empty!");}
        if (isBlank(task.getName())) throw new CustomException("Name is required!");
        if (isBlank(task.getDescription())) throw new CustomException("Description is required!");
        if (task.getDueDate() == null) throw new CustomException("Due Date is required!");
        if (task.getDueDate().isBefore(LocalDate.now())){throw new CustomException("due date should not be in the past!");}
        if (task.getStatus() != null) e.setStatus(task.getStatus());
        if (task.getPriority() != null) e.setPriority(task.getPriority());
    }
    private void validateUpdate(Long id, TaskUpdate update, TaskEntity task){
        validateID(id);
        if (update == null) throw new CustomException("Request Body is empty");
        boolean hasAny = notBlank(update.getName()) || notBlank(update.getDescription())
                || update.getPriority() != null || update.getStatus() != null || update.getDueDate() != null;
        if (!hasAny) throw new CustomException("At least 1 field should be provided");
        if (update.getDueDate().isBefore(LocalDate.now())) throw new CustomException("due date should not be in the past!");
    }
    private void validateID(Long id) {if (id == null || id <= 0) throw new CustomException("Invalid id");}
}
