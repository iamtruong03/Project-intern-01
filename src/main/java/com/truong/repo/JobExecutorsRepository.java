package com.truong.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truong.entities.JobExecutors;

@Repository
public interface JobExecutorsRepository extends JpaRepository<JobExecutors, Long> {

	boolean existsByUserId(Long userId);

}
