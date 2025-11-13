package com.openclassrooms.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileService {
	
	// path for stoking files, in the root of the app	
	private final Path uploadDir = Paths.get("uploads");

	// create folder for stocking files if it does not exist	
    public FileService() throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    public String save(MultipartFile file) throws IOException {
        // create a unique filename
        String originalFilename = file.getOriginalFilename();
        String filename = System.currentTimeMillis() + "_" + originalFilename;

        // save file to disk
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String baseUrl = ServletUriComponentsBuilder
        		.fromCurrentContextPath()
                .build()
                .toUriString();
        
        // return file's URL
        return baseUrl + "/uploads/" + filename;
    }
}
