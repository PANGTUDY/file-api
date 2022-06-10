package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.example.demo.service.DocumentService;

@RestController
@RequestMapping("/board")
public class DocumentController {
	@Autowired
	DocumentService documentService;
	
	// =========================== GET ===========================
	@GetMapping("/posts/{post_id}")
	public List<Document> get_document_info(@PathVariable(name="post_id", required=true) Long postId){
		return documentService.getDocsInfo(postId);
	}
	
	@GetMapping("/posts/{post_id}/{file_id}")
	public ResponseEntity<Resource> download_one(@PathVariable(name="post_id", required=true) Long postId,
								 @PathVariable(name="file_id", required=true) Long fileId) throws IOException {
		// [todo] : add post_id to where clause
		Document docuFile = documentService.findById(fileId);
		return documentService.downloadFile(docuFile);
	}
	
	@GetMapping("/{post_id}/download/all")
	public String download_all(@PathVariable int postId) {
		return "implementing...";
	}
	
	// =========================== POST ===========================
	@PostMapping("/posts/{post_id}")
	public List<Document> upload(@PathVariable(name="post_id", required=true) long postId, @RequestParam MultipartFile[] ufile) throws IllegalStateException, IOException {
		
		// check MultipartFile exist
		List<MultipartFile> received = new ArrayList<>();
		for(MultipartFile mf : ufile) {
			if(mf.isEmpty()) continue;
			received.add(mf);
		}
		
		if(!received.isEmpty())
			return documentService.uploadFile(postId, received);
		return Collections.EMPTY_LIST;
	}
	
	// =========================== PUT ===========================
	@PutMapping("/posts/{post_id}")
	public List<Document> modify(@PathVariable(name="post_id", required=true) long postId, @RequestParam MultipartFile[] ufile) throws IllegalStateException, IOException {
		documentService.deleteDocument(postId);
		
		// check MultipartFile exist
		List<MultipartFile> received = new ArrayList<>();
		for(MultipartFile mf : ufile) {
			if(mf.isEmpty()) continue;
			received.add(mf);
		}
		
		// [todo] ufile data zero check logic
		if(!received.isEmpty())
			return documentService.uploadFile(postId, received);
		return Collections.EMPTY_LIST;
	}
	
	// =========================== DELETE ===========================
	@DeleteMapping("/posts/{post_id}")
	public List<Document> deletePostDocument(@PathVariable(name="post_id", required=true) long postId) {
		documentService.deleteDocument(postId);
		return Collections.EMPTY_LIST;
	}
	
}
