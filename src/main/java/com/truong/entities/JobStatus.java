package com.truong.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "jobStatus")
public class JobStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobStatusId;
	private String jobStatusName;

}
