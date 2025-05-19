package com.example.server.repository;

import com.example.server.model.PGroup;
import com.example.server.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerUserId(Long userId);

    List<Project> findByOwnerGroupId(Long id);

}
