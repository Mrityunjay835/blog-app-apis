 package com.project.blog.payloads;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	
	private int id;
	@NotEmpty
	private String name;
	@Email
	@Size(min = 4, message="Email not valid!")
	private String email;
	@NotEmpty
	@Size(min =4, max =10, message="must be between 4 to 10")
	private String password;
	@NotEmpty
	private String about;

}
