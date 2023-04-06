package com.project.blog.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="post_table")
@NoArgsConstructor
@Getter
@Setter

public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int postId;
	@Column(name="post_title",nullable = false)
	private String title;
	@Column(name="post_content", nullable=false, length= 1000)
	private String content;
	private String imagName;
	private Date date;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@JoinColumn(name="category_id")
	@ManyToOne
	private Category category;
	
	@OneToMany(mappedBy = "post",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private Set<Comment> comments=new HashSet<Comment>();

}
