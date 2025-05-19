package com.example.server.controller;

import com.example.server.model.PGroup;
import com.example.server.model.Project;
import com.example.server.model.User;
import com.example.server.repository.PGroupRepository;
import com.example.server.repository.ProjectRepository;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
public class PGroupController {

    @Autowired
    private PGroupRepository groupRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjectRepository projectRepo;

    // Create a new group
    @PostMapping("/create")
    public PGroup createGroup(@RequestBody PGroup group) {
        if (group.getMembers() == null ){
            group.setMembers(new ArrayList<>());
        }
        if (group.getAdmin() != null && !group.getMembers().contains(group.getAdmin())) {
            group.getMembers().add(group.getAdmin());
        }
        return groupRepo.save(group);
    }

    // Add a user to a group
    @PostMapping("/{groupId}/addUser/{userId}")
    public String addUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {

        Optional<PGroup> group = groupRepo.findById(groupId);
        Optional<User> user = userRepo.findById(userId);

        if (group.isPresent() && user.isPresent()) {
            group.get().getMembers().add(user.get());
            groupRepo.save(group.get());
            return "User added to group.";
        } else {
            return "Group or User not found.";
        }
    }

    // Remove a user from a group
    @PostMapping("/{groupId}/removeUser/{userId}")
    public String removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {

        Optional<PGroup> group = groupRepo.findById(groupId);
        Optional<User> user = userRepo.findById(userId);

        if (group.isPresent() && user.isPresent()) {
            group.get().getMembers().remove(user.get());
            groupRepo.save(group.get());
            return "User removed from group.";
        } else {
            return "Group or User not found.";
        }
    }

    @GetMapping("/{groupId}/projects")
    public List<Project> getGroupProjects(@PathVariable Long groupId) {
        PGroup group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        return projectRepo.findByOwnerGroupId(group.getId());
    }


    // Get all users in a group
    @GetMapping("/{groupId}/members")
    public List<User> getGroupMembers(@PathVariable Long groupId) {
        Optional<PGroup> group = groupRepo.findById(groupId);
        return group.map(PGroup::getMembers).orElse(null);
    }

    // Get all groups a user is in
    @GetMapping("/user/{userId}")
    public List<PGroup> getUserGroups(@PathVariable Long userId) {
        return groupRepo.findGroupsByUserId(userId);
    }

    @PutMapping("/{groupId}")
    public PGroup updateGroup(@PathVariable Long groupId, @RequestBody PGroup updatedData) {
        PGroup group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(updatedData.getName());
        group.setDescription(updatedData.getDescription());
        return groupRepo.save(group);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) {
        Optional<PGroup> groupOptional = groupRepo.findById(groupId);
        if (groupOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found.");
        }

        PGroup group = groupOptional.get();

        // 1. Remove all user-group associations
        group.getMembers().clear();
        groupRepo.save(group); // Persist clearing of join table (user_pgroup)

        // 2. Delete all group projects (which deletes tasks too, by cascade in your model)
        List<Project> groupProjects = projectRepo.findByOwnerGroupId(groupId);
        for(Project project : groupProjects) {
            projectRepo.deleteById(project.getId());
        }
        // 3. Finally delete the group itself
        groupRepo.delete(group);

        return ResponseEntity.ok("Group and all related data deleted.");
    }




}
