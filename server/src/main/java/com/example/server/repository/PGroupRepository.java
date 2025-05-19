package com.example.server.repository;

import com.example.server.model.PGroup;
import com.example.server.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PGroupRepository extends JpaRepository<PGroup, Long> {

    @Query(value = "SELECT * FROM pgroup WHERE id IN ( SELECT pgroup_id FROM user_pgroup WHERE user_id = :userId )",
            nativeQuery = true
    )
    List<PGroup> findGroupsByUserId(@Param("userId") long userid);

}
