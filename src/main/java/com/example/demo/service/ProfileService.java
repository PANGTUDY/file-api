package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Document;
import com.example.demo.entity.Profile;
import com.example.demo.repository.ProfileRepository;

@Service
public class ProfileService {
	@Autowired
	private ProfileRepository profileRepository;		// JpaRepository : DAO
	
	@Value("${spring.servlet.multipart.location}")
	String locationRoot;
	
	@Value("${file.document}")
	String documentDirectory;
	
	@Value("${file.profile}")
	String profileDirectory;
	
	long notExist = -1;
	
	public Profile uploadProfile(long userId, MultipartFile profileImg) throws IllegalStateException, IOException{
		Profile profile = Profile.builder().uuId(UUID.randomUUID().toString()) // Builder Pattern 적용
											.fileName(profileImg.getOriginalFilename())
											.contentType(profileImg.getContentType())
											.fileSize(profileImg.getSize())
											.userId(userId)
											.build();
		
		File targetFile = new File(profileDirectory + 			// Directory Folder 지정
									File.separator + 
									profile.getUuId() + 
									profile.getFileName());	// 실제 물리 저장소에 저장
		
		profileImg.transferTo(targetFile);
		profileRepository.save(profile);
		return profile;
	}
	
	public ResponseEntity<Resource> downloadFile(Profile atchFile) throws IOException {	
		Path path = Paths.get(locationRoot + profileDirectory + File.separator + atchFile.getUuId() + atchFile.getFileName());
		
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentDisposition(ContentDisposition.builder("attachment")
															.filename(atchFile.getFileName(), StandardCharsets.UTF_8)
															.build());
		String contentType = Files.probeContentType(path);
		httpHeader.add(HttpHeaders.CONTENT_TYPE, contentType);
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return new ResponseEntity<>(resource, httpHeader, HttpStatus.OK);
	}
	
	public Profile getUserProfile(long userId) {
		return profileRepository.findByUserId(userId);
	}
	
	public Profile deleteUserProfile(long userId) {
		Profile profile = profileRepository.findByUserId(userId);
		File file = new File(locationRoot + File.separator + profileDirectory + File.separator + profile.getUuId() + profile.getFileName());
		if(file.exists()) file.delete();
		profileRepository.delete(profile);
		return profile;
	}
}
