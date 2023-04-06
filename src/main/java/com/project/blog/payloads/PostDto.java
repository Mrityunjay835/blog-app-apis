package com.project.blog.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	@NotEmpty
	private String title;
	@Size(min=4, max= 1000,message="content not be empty!")
	
	private String content;
	private String imagName;
	
	private Date date;
	private CategoryDto category;
	private UserDto user;
	
	private Set<CommentDto> comments=new HashSet<CommentDto>();
}
