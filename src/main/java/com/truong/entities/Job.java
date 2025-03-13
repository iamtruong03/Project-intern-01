package com.truong.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "job")
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobId;

	private String jobName;

	@ManyToMany
	@JoinTable(name = "job_executors", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonIgnoreProperties({ "address", "fullName", "password", "department", "username" })
	private Set<User> executedUsers = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "approver_id")
	@JsonIgnoreProperties({ "address", "fullName", "password", "department", "username" })
	private User approverId;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private JobStatus jobStatus;

	private LocalDate deadline;
	
}
