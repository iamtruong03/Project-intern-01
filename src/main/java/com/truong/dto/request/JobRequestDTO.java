package com.truong.dto.request;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobRequestDTO {
	private String jobName;
	private Long createdId;
	private Long executedId;
	private String status;
	private LocalDate deadline;


}