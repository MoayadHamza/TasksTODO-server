package com.example.server.controller;

import com.example.server.model.ImportanceLevel;
import com.example.server.model.Project;
import com.example.server.model.Task;
import com.example.server.model.TaskStatus;
import com.example.server.repository.ProjectRepository;
import com.example.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ProjectRepository projectRepo; // ➔ ADDED: to link tasks to projects

    // GET all
    @GetMapping
    public List<Task> getAllTasks() {

        return taskRepo.findAll();
    }

    // GET one
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {

        return taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    // GET filtered (fixed the path to /filter)
    @GetMapping("/filter")
    public List<Task> getFilteredTasks(
            @RequestParam(required = false) TaskStatus completed,
            @RequestParam(required = false) ImportanceLevel importance
    )
    {

        if (completed != null && importance != null) {
            return taskRepo.findByCompletedAndImportance(completed, importance);
        } else if (completed != null) {
            return taskRepo.findByCompleted(completed);
        } else if (importance != null) {
            return taskRepo.findByImportance(importance);
        } else {
            return taskRepo.findAll();
        }
    }

    // POST ➔ Create a task inside a project
    @PostMapping("/project/{projectId}")
    public Task createTask(@PathVariable Long projectId, @RequestBody Task task) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));
        task.setProject(project); // ➔ Set project before saving
        return taskRepo.save(task);
    }

    // PUT
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {

        Task task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.getCompleted());
        task.setImportance(updatedTask.getImportance());
        // Note: we don't allow moving the task to another project during update for now
        return taskRepo.save(task);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {

        if (!taskRepo.existsById(id)) {
            throw new RuntimeException("Task not found with id " + id);
        }
        taskRepo.deleteById(id);
    }
}
