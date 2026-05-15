package com.velocity_flow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.velocity_flow.api.entity.Sprint;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

	List<Sprint> findByProjectIdIn(List<Long> projectIds);

}
