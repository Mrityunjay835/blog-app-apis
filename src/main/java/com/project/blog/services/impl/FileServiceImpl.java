package com.project.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile multipartFile) throws IOException {
		//get  File name
		String name= multipartFile.getOriginalFilename();  //abc.png
		
		//random name generator file
		String randomId = UUID.randomUUID().toString();	
		String fileName = randomId.concat(name.substring(name.lastIndexOf(".")));
		
		//full path name
		String filePath= path+ File.separator+ fileName;
		
		
		//create if folder not exit
		File f = new File(path);
		if(!f.exists()) f.mkdir();
		
		//file copy form the post to folder	
		Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
			
		return fileName;
	}

	@Override
	public InputStream getResourse(String path, String fileName) throws FileNotFoundException {
		String fullPath = path+ File.separator+fileName;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

}
