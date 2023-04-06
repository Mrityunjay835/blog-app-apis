package com.project.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.blog.entities.Comment;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.CommentDto;
import com.project.blog.repositories.CommentRepo;
import com.project.blog.repositories.PostRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.CommentService;


@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Comment dtoToComment(CommentDto commentDto) {
		return this.modelMapper.map(commentDto, Comment.class);
	}
	public CommentDto commentToDto(Comment comment) {
		return this.modelMapper.map(comment, CommentDto.class);
	}
	
	@Override
	public CommentDto createComment(CommentDto commentDto,Integer userId, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id", postId));
		User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id", userId));

		Comment comment = this.dtoToComment(commentDto);
		comment.setUser(user);
		comment.setPost(post);
		Comment addComment =this.commentRepo.save(comment);
		return this.commentToDto(addComment);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","comment id", commentId));
		this.commentRepo.delete(comment);
	}

	

}
