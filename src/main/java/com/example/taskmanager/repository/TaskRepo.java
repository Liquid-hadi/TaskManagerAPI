package com.example.taskmanager.repository;

import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.enums.TaskStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long> {

    //Soft Delete
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update TaskEntity t set t.status = com.example.taskmanager.enums.TaskStatus.DELETED where t.Id = :id")
    int softDeleteById(@Param("id") Long id);

    List<TaskEntity> findAllByStatusNot(TaskStatus status);

}
