package com.project.blog.services;

import java.util.List;

import com.project.blog.payloads.PostDto;
import com.project.blog.payloads.PostResponse;

public interface PostService {
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);  //completed
	PostDto updatePost(PostDto postDto, Integer postId);    //completed
	PostDto getPostById(Integer postId);    //completed
	void deletePost(Integer postId);        //completed
	PostResponse getAllPost(int pageNumber ,int pageSize,String sortBy,String sortDir);            //completed
	
	List<PostDto> getAllPostByCategory(Integer categoryId);  //completed
	List<PostDto> getAllPostByUser(Integer userId);          //completed
	
	List<PostDto> searchPosts(String serchKeyword);     //completed
	
	
}
