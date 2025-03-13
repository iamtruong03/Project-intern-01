package com.truong.dto;

import com.truong.entities.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
	private Long id;
	private String fullName;
	private String userName;
	private String password;
	private String address;
	private String nameDepartment;
	private Long departmentId;

	public static UserDTO fromEntity(User user) {
		if (user == null) {
			return null;
		}
		return new UserDTO(user.getId(), user.getFullName(), user.getUserName(), user.getPassword(), user.getAddress(),
				user.getDepartment() != null ? user.getDepartment().getNameDepartment() : null,
				user.getDepartment() != null ? user.getDepartment().getDepartmentId() : null);
	}

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, String fullName, String userName, String password, String address, String nameDepartment,
			Long departmentId) {
		this.id = id;
		this.fullName = fullName;
		this.userName = userName;
		this.password = password;
		this.address = address;
		this.nameDepartment = nameDepartment;
		this.departmentId = departmentId;
	}
	
//	public UserDTO(Long id, String fullName, String userName, String password, String address, String nameDepartment) {
//		this.id = id;
//		this.fullName = fullName;
//		this.userName = userName;
//		this.password = password;
//		this.address = address;
//		this.nameDepartment = nameDepartment;
//	}

	public UserDTO(User user) {
		if (user != null) {
			this.id = user.getId();
			this.fullName = user.getFullName();
			this.userName = user.getUserName();
			this.password = user.getPassword();
			this.address = user.getAddress();
			this.nameDepartment = user.getDepartment() != null ? user.getDepartment().getNameDepartment() : null;
			this.departmentId = user.getDepartment() != null ? user.getDepartment().getDepartmentId() : null;
		}
	}

	
}
