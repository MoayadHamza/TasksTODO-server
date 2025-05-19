package com.example.server.controller;

import com.example.server.model.*;
import com.example.server.repository.PGroupRepository;
import com.example.server.repository.ProjectRepository;
import com.example.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private PGroupRepository groupRepo;

    @Autowired
    private TaskController taskCont;

    // --- Project Endpoints ---

    // GET all projects
    @GetMapping
    public List<Project> getAllProjects() {

        return projectRepo.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Project> getProjectsByUser(@PathVariable Long userId) {
        return projectRepo.findByOwnerUserId(userId);
    }


    // GET one project by ID
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {

        return projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // POST create new project
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectRepo.save(project);
    }

    @PostMapping("/group/{groupId}")
    public Project createGroupProject(@PathVariable Long groupId, @RequestBody Project project) {
        PGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));

        project.setOwnerGroup(group);
        return projectRepo.save(project);
    }


    // PUT update project
    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {

        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
        project.setName(updatedProject.getName());
        project.setDescription((updatedProject.getDescription()));
        return projectRepo.save(project);
    }

    // DELETE project
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        if (!projectRepo.existsById(id)) {
            throw new RuntimeException("Project not found with id " + id);
        }
        List<Task> tasks = getTasksInProject(id);
        for (Task task : tasks) {
            taskRepo.deleteById(task.getId());
        }
        projectRepo.deleteById(id);
    }

    // --- Task Endpoints inside Project ---

    // GET all tasks inside a project
    @GetMapping("/{projectId}/tasks")
    public List<Task> getTasksInProject(@PathVariable Long projectId) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));
        return taskRepo.findByProject(project);
    }

    // GET filtered tasks inside a project
    @GetMapping("/{projectId}/filter")
    public List<Task> getFilteredTasksInProject(
            @PathVariable Long projectId,
            @RequestParam(required = false) TaskStatus completed,
            @RequestParam(required = false) ImportanceLevel importance
    ) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + projectId));

        if (completed != null && importance != null) {
            return taskRepo.findByProjectAndCompletedAndImportance(project, completed, importance);
        } else if (completed != null) {
            return taskRepo.findByProjectAndCompleted(project, completed);
        } else if (importance != null) {
            return taskRepo.findByProjectAndImportance(project, importance);
        } else {
            return taskRepo.findByProject(project);
        }
    }
}
