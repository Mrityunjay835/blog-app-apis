package com.project.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.blog.entities.Category;
import com.project.blog.entities.Post;
import com.project.blog.entities.User;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.PostDto;
import com.project.blog.payloads.PostResponse;
import com.project.blog.repositories.CategoryRepo;
import com.project.blog.repositories.PostRepo;
import com.project.blog.repositories.UserRepo;
import com.project.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;
	

	public Post dtoToPost(PostDto postDto) {
		return this.modelMapper.map(postDto, Post.class);
	}

	public PostDto postToDto(Post post) {
		return this.modelMapper.map(post, PostDto.class);
	}

//create the post .....................................................................
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));
		 Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

		Post post = dtoToPost(postDto);
		post.setImagName("default.png");
		post.setDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post createdPost = this.postRepo.save(post);
		return postToDto(createdPost);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post =this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImagName(postDto.getImagName());
		
		Post updatedPost=this.postRepo.save(post);
		return postToDto(updatedPost);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));		
		return postToDto(post);
		
	}

	@Override
	public void deletePost(Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","psotId",postId));
		this.postRepo.delete(post);
		

	}

	@Override
	public PostResponse getAllPost(int pageNumber ,int pageSize,String sortBy,String sortDir) {
		//pagination jpsRepository extends Pagination(PagingAndSortingRepository) go-check-it
		
		Sort sort  =sortDir.equalsIgnoreCase("dsc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		
		Page<Post> pagePosts = this.postRepo.findAll(p);
		
		List<Post> allPost = pagePosts.getContent();
		List<PostDto> postDtos = allPost.stream().map(post-> postToDto(post)).collect(Collectors.toList());
		
		PostResponse postResponse =new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElement(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setLastPage(pagePosts.isLast());
		
		return postResponse;
	}

	@Override
	public List<PostDto> getAllPostByCategory(Integer categoryId) {
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Categury Id",categoryId));
		List<Post> posts = this.postRepo.findByCategory(category);
		List<PostDto> postDtos= posts.stream().map(post-> postToDto(post)).collect(Collectors.toList());
		
		return postDtos;
	}

	@Override
	public List<PostDto> getAllPostByUser(Integer userId) {
		User user =this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));
		List<Post> posts= this.postRepo.findByUser(user);
		List<PostDto> postDtos= posts.stream().map(post->postToDto(post)).collect(Collectors.toList());
		
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String serchKeyword) {
		List<Post> posts=this.postRepo.findByTitle("%"+serchKeyword+"%");
		List<PostDto> postDto = posts.stream().map(post-> postToDto(post)).collect(Collectors.toList());
		return postDto;
	}

}
