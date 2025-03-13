package com.truong.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class InvalidatedToken {
	@Id
	private String id;
	private Date expiryTime;
}
