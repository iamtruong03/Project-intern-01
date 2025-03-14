package com.truong.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@Table(name = "job")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Job {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long jobId;

	String jobName;

//	List<String> executedUsersIds = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "job_executors", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonIgnoreProperties({ "address", "fullName", "password", "department", "username" })
	Set<User> executedUsers = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "approver_id")
	@JsonIgnoreProperties({ "address", "fullName", "password", "department", "username" })
	User approverId;

	@ManyToOne
	@JoinColumn(name = "status_id")
	JobStatus jobStatus;

	LocalDate deadline;
	
}
