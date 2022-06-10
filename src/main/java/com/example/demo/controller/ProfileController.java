package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Document;
import com.example.demo.entity.Profile;
import com.example.demo.service.ProfileService;

@RestController
@RequestMapping("/users")
public class ProfileController {
	
	@Autowired
	ProfileService profileService;
	
	// =========================== POST ===========================
	@PostMapping("/{user_id}")
	public Profile upload(@PathVariable(name="user_id", required=true) long userId, @RequestParam MultipartFile profile) 
								throws IllegalStateException, IOException {
		return profileService.uploadProfile(userId, profile);
	}
	
	// =========================== GET ===========================
	@GetMapping("/profile/{user_id}")
	public Profile get_profile_info(@PathVariable(name="user_id", required=true) Long userId){
		return profileService.getUserProfile(userId);
	}
	
	@GetMapping("/{user_id}")
	public ResponseEntity<Resource> download_one(@PathVariable(name="user_id", required=true) Long userId,
								 @PathVariable(name="file_id", required=true) Long fileId) throws IOException {
		// [todo] : add post_id to where clause
		Profile proFile = profileService.getUserProfile(userId); // 프로필 첫 번째 요소
		return profileService.downloadFile(proFile);
	}
	
	// =========================== PUT ===========================
	@PutMapping("/{user_id}")
	public Profile update_profile(@PathVariable(name="user_id", required=true) Long userId, @RequestParam MultipartFile profile) throws IllegalStateException, IOException {
		Profile prevProfile = profileService.deleteUserProfile(userId);
		if(!profile.isEmpty()) profileService.uploadProfile(userId, profile);
		return prevProfile;
	}
	
	// =========================== DELETE ===========================
	@DeleteMapping("/{user_id}")
	public Profile delete_profile(@PathVariable(name="user_id", required=true) Long userId) {
		return profileService.deleteUserProfile(userId);
	}
}
