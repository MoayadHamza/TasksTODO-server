package com.example.server.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
//    @CreationTimestamp
//    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ownerUser; // Private project owner
    @ManyToOne
    @JoinColumn(name = "pgroup_id")
    private PGroup ownerGroup; // Group project owner
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;


    // Constructors
    public Project() {
        // No need to set createdAt manually, Hibernate will do it
    }
    public Project(String name, String description, User ownerUser, PGroup ownerGroup) {
        this.name = name;
        this.description = description;
        this.ownerUser = ownerUser;
        this.ownerGroup = ownerGroup;
    }


    // Getters and Setters
    public Long getId() {return id;}
    public String getName() {return name;}
//    public User getOwnerUser() {return ownerUser;}
    public PGroup getOwnerGroup() {return ownerGroup;}
    public List<Task> getTasks() {return tasks;}
    public void setName(String name) {this.name = name;}
//    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public void setOwnerUser(User ownerUser) {this.ownerUser = ownerUser;}
    public void setOwnerGroup(PGroup ownerGroup) {this.ownerGroup = ownerGroup;}
    public void setTasks(List<Task> tasks) {this.tasks = tasks;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
