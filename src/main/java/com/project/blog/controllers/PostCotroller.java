package com.project.blog.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.blog.config.AppConstants;
import com.project.blog.payloads.ApiResponse;
import com.project.blog.payloads.PostDto;
import com.project.blog.payloads.PostResponse;
import com.project.blog.services.FileService;
import com.project.blog.services.PostService;

@RestController
@RequestMapping("/api/")
public class PostCotroller {
	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;
	@Value("${project.image}")
	private String path;

	@PostMapping("/user/{userId}/category/{categoryId}/posts/")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}
//............................................................................................
//............................................................................................
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostByUser(@PathVariable Integer userId) {
		List<PostDto> postByUsers = this.postService.getAllPostByUser(userId);
		return new ResponseEntity<List<PostDto>>(postByUsers, HttpStatus.OK);
	}

	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getAllPostByCategory(@PathVariable Integer categoryId) {
		List<PostDto> postByCategory = this.postService.getAllPostByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(postByCategory, HttpStatus.OK);
	}
//............................................................................................
//............................................................................................
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatedPostDto = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPostDto, HttpStatus.OK);
	}

	@DeleteMapping("/posts/{postId}")
	public ApiResponse deletePost(@PathVariable Integer psotId) {
		this.postService.deletePost(psotId);
		return new ApiResponse("post is successfully deleted !", true);
	}
//............................................................................................
//............................................................................................
	@GetMapping("/posts/search/{searchKeyword}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable String searchKeyword) {
		List<PostDto> postDtos = this.postService.searchPosts(searchKeyword);
		return new ResponseEntity<List<PostDto>>(postDtos, HttpStatus.OK);
	}
//............................................................................................
//............................................................................................
	@PostMapping("/posts/file/upload/{postId}")
	public ResponseEntity<PostDto> uploadImage(@RequestParam("image") MultipartFile multipartFile,
			@PathVariable Integer postId) throws IOException {
		PostDto postDto= this.postService.getPostById(postId); 
		
		String fileName=this.fileService.uploadImage(path, multipartFile);
		
		postDto.setImagName(fileName);
		PostDto updatedPost=this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost,HttpStatus.OK);
	}
	
	@GetMapping(value="/profiles/{imageName}",produces = MediaType.IMAGE_PNG_VALUE)
	public void downloadImage(@PathVariable("imageName")String imageName,
			HttpServletResponse response) throws IOException {
		InputStream resourse = this.fileService.getResourse(path, imageName);
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		
		StreamUtils.copy(resourse, response.getOutputStream());
		
	}
	

}






