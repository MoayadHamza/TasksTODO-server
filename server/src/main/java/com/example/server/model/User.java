package com.example.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    // Many-to-many relationship between User and Group
    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private List<PGroup> groups;

    // constructors
    public User() {}
    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters and Setter
    public Long getId() { return id; }
    public String getUsername() {return username;}
    public String getFullName() {return fullName;}
    public String getPassword() {return password;}
    public List<PGroup> getGroups() {return groups;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public void setPassword(String password) {this.password = password;}
    public void setUsername(String username) {this.username = username;}
    public void setGroups(List<PGroup> groups) {this.groups = groups;}

    @Override
    public String toString() {
        return "" + this.id + " " + this.fullName + " " + this.username + " " + this.password + " " + this.groups;
    }

}
