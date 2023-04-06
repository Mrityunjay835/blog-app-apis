package com.project.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.blog.entities.Category;
import com.project.blog.exceptions.ResourceNotFoundException;
import com.project.blog.payloads.CategoryDto;
import com.project.blog.repositories.CategoryRepo;
import com.project.blog.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		return categoryToDto(this.categoryRepo.save(dtoToCategory(categoryDto)));
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category Id", categoryId));
		category.setTitle(categoryDto.getTitle());
		category.setCategoryDescription(categoryDto.getTitle());
		this.categoryRepo.save(category);
		return categoryToDto(category);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryId));
		this.categoryRepo.delete(category);
	}

	@Override
	public CategoryDto getCategoryById(Integer categoryId) {
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id ", categoryId));

		return categoryToDto(category);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> allCategory = this.categoryRepo.findAll();
		List<CategoryDto> res = allCategory.stream().map(category -> categoryToDto(category))
				.collect(Collectors.toList());

		return res;
	}

	public Category dtoToCategory(CategoryDto categoryDto) {
		return this.modelMapper.map(categoryDto, Category.class);
	}

	public CategoryDto categoryToDto(Category category) {
		return this.modelMapper.map(category, CategoryDto.class);
	}

}
