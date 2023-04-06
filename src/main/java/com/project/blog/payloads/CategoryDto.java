package com.project.blog.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {
	private int id;
	@NotEmpty
	@Size(max=20,message="please pass the title")
	private String title;
	@Size(min=4,max=50, message="write something about")
	private String categoryDescription;
}
