package com.example.taskmanager.spec;

import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TaskSpecifications {

    public static Specification<TaskEntity> hasPriority(TaskPriority priority){
        return (root, query, cb) -> {
            if (priority == null) {
                return cb.conjunction(); // no filtering
            }
            return cb.equal(root.get("priority"), priority);
        };
    }
    public static Specification<TaskEntity> hasSatus(TaskStatus status){
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction(); // no filtering
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<TaskEntity> SearchQ(String q){
        return (root, query, cb) -> {
            if (q == null || q.trim().isEmpty()) {
                return cb.conjunction(); // no filtering
            }
            String like = "%" + q.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("description")), like)
            );
        };
    }

    public static Specification<TaskEntity> notDeleted() {
        return (root, query, cb) ->
                cb.notEqual(root.get("status"), TaskStatus.DELETED);
    }
    public static Specification<TaskEntity> dueDateBetween(LocalDate from, LocalDate to){
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }
            Path<LocalDate> due = root.get("dueDate");

            if (from != null && to != null) {
                return cb.between(due, from, to); // inclusive
            }

            if (from != null){
                return cb.greaterThanOrEqualTo(due,from);
            }
            return cb.lessThanOrEqualTo(due,to);
    };

}
}
