package com.project.blog.services;

import com.project.blog.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto,Integer userId, Integer postId);
	void deleteComment(Integer commentId);
}
