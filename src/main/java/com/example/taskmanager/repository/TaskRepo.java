package com.example.taskmanager.repository;

import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    //Soft Delete
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update TaskEntity t set t.status = com.example.taskmanager.enums.TaskStatus.DELETED where t.Id = :id")
    int softDeleteById(@Param("id") Long id);

//    List<TaskEntity> findAllByStatusNot(TaskStatus status, Pageable pageable);
/*    Page<TaskEntity> findAllByFilter(FilterParameter filterParameter){
                "Select * from taskEntity where "
                filterStatus(filterParameter);
                return
            }
            private void filterStatus(FilterParameter filterParameter){
                if (filterParameter.getStatus() != null){
                    append.("te.Status ="+ filterParameter.getStatus)
        }*/
    }

