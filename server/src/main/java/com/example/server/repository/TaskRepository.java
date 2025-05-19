package com.example.server.repository;

import com.example.server.model.Task;
import com.example.server.model.TaskStatus;
import com.example.server.model.ImportanceLevel;
import com.example.server.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompleted(TaskStatus completed);

    List<Task> findByImportance(ImportanceLevel importance);

    List<Task> findByCompletedAndImportance(TaskStatus completed, ImportanceLevel importance);

    // ➔ NEW: Find tasks by project
    List<Task> findByProject(Project project);

    // ➔ NEW: Find tasks by project and status
    List<Task> findByProjectAndCompleted(Project project, TaskStatus completed);

    // ➔ NEW: Find tasks by project and importance
    List<Task> findByProjectAndImportance(Project project, ImportanceLevel importance);

    List<Task> findByProjectAndCompletedAndImportance(Project project, TaskStatus completed, ImportanceLevel importance);

    void deleteByUserIdAndGroupIsNull(Long userId);



}
