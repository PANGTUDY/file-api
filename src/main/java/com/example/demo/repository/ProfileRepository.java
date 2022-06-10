package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{
	
	Profile findByUserId(Long userId);
}
