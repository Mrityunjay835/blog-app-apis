package com.project.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.blog.repositories.UserRepo;

@SpringBootTest
class BlogAppApisApplicationTests {

	@Autowired
	private UserRepo userRepo;
	
	@Test
	void contextLoads() {
	}

	
	@Test
	public void repoTest() {
		String nameOfClass=this.userRepo.getClass().getName();
		System.out.println(nameOfClass);
		String nameOfPackage =this.userRepo.getClass().getPackageName();
		System.out.println(nameOfPackage);
}
}
