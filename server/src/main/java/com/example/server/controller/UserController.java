package com.example.server.controller;

import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import com.example.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TaskRepository taskRepo;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            return "Username already taken.";
        }
        // Trim spaces from password
        user.setPassword(user.getPassword().replace(" ", ""));
        userRepo.save(user);
        return "User registered successfully.";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> found = userRepo.findByUsername(user.getUsername());
        if (found.isPresent() && found.get().getPassword().equals(user.getPassword())) {
            User result = found.get();
            result.setPassword(null); // Avoid sending back password
            System.out.println(result.toString());
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userRepo.findById(id);
    }

    @GetMapping("/username/{username}")
    public Optional<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userResult = userRepo.findByUsername(username);
        System.out.println(userResult);
        return  userResult;
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

        Optional<User> existing = userRepo.findById(id);
        if (existing.isPresent()) {
            User user = existing.get();
            // Don't change username
            user.setFullName(updatedUser.getFullName());
            if(updatedUser.getPassword() != null){
                user.setPassword(updatedUser.getPassword().replace(" ", ""));
            }
            userRepo.save(user);
            return "User updated.";
        } else {
            return "User not found.";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {

        if (userRepo.existsById(id)) {
            // Delete personal tasks first
            taskRepo.deleteByUserIdAndGroupIsNull(id);
            // Then delete user
            userRepo.deleteById(id);
            return "User and associated personal tasks deleted.";
        } else {
            return "User not found.";
        }
    }
}
