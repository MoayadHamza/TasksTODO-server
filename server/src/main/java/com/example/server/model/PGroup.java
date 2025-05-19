package com.example.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
public class PGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
    @Column(nullable = false)
    private String name;
    private String description;
//    @CreationTimestamp
//    private LocalDateTime createdAt;
    // Many-to-many relationship between Group and User
    @ManyToMany
    @JoinTable(
            name = "user_pgroup", // Join table name
            joinColumns = @JoinColumn(name = "pgroup_id"), // Column in the join table for Group
            inverseJoinColumns = @JoinColumn(name = "user_id") // Column in the join table for User
            )
    @JsonIgnore
    private List<User> members;


    // constructors
    public PGroup() {}
    public PGroup(User admin, String name, String description) {
        this.admin = admin;
        this.name = name;
        this.description = description;
    }

    // Getters and Setter
    public Long getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public User getAdmin() {return admin;}
    public List<User> getMembers() {return members;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}
    public void setAdmin(User admin) {this.admin = admin;}
    public void setMembers(List<User> members) {this.members = members;}

}
