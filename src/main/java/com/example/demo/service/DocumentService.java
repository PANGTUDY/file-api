package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.example.demo.repository.DocumentRepository;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository documentRepository;		// JpaRepository : DAO
	
	@Value("${spring.servlet.multipart.location}")
	String locationRoot;
	
	@Value("${file.document}")
	String documentDirectory;
	
	@Value("${file.profile}")
	String profileDirectory;
	
	long notExist = -1;
	
	// 데이터베이스에 기록된 모든 파일 정보 조회
	public List<Document> findAll(){
		List<Document> files = new ArrayList<>();
		for(Document file : documentRepository.findAll()) files.add(file);
		return files;
	}
	
	public List<Document> getDocsInfo(Long postId) {
		List<Document> files = new ArrayList<>();
		for(Document file : documentRepository.findAllByPostId(postId)) files.add(file);
		return files;
	}
	
	public Document findById(Long fileId) {
		Optional<Document> file = documentRepository.findById(fileId);
		if(file.isEmpty()) return Document.builder().fileId(notExist).build();
		else return file.get();
	}
	
	public List<Document> uploadFile(long postId, List<MultipartFile> received) throws IllegalStateException, IOException{
		List<Document> files = new ArrayList<>();
		
		for(MultipartFile file : received) {
			Document atchFile = Document.builder().uuId(UUID.randomUUID().toString()) // Builder Pattern 적용
												.fileName(file.getOriginalFilename())
												.contentType(file.getContentType())
												.fileSize(file.getSize())
												.postId(postId)
												.build();
			
			files.add(atchFile);	// 저장할 파일 정보 저장
			File targetFile = new File(documentDirectory + 			// Directory Folder 지정
										File.separator + 
										atchFile.getUuId() + 
										atchFile.getFileName());	// 실제 물리 저장소에 저장
			file.transferTo(targetFile);
		}
		
		documentRepository.saveAll(files);
		return files;
	}
	
	public ResponseEntity<Resource> downloadFile(Document atchFile) throws IOException {	
		Path path = Paths.get(locationRoot + documentDirectory + File.separator + atchFile.getUuId() + atchFile.getFileName());
		
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentDisposition(ContentDisposition.builder("attachment")
															.filename(atchFile.getFileName(), StandardCharsets.UTF_8)
															.build());
		String contentType = Files.probeContentType(path);
		httpHeader.add(HttpHeaders.CONTENT_TYPE, contentType);
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		return new ResponseEntity<>(resource, httpHeader, HttpStatus.OK);
	}
	
	public void deleteDocument(Long postId) {
		List<Document> documentFiles = documentRepository.findAllByPostId(postId);
		
		// 사용자 폴더에서도 삭제
		for(Document docs : documentFiles) {
			String filePath = locationRoot + File.separator + documentDirectory + File.separator + docs.getUuId() + docs.getFileName();
			File delTargetFile = new File(filePath);
			if(delTargetFile.exists()) delTargetFile.delete();
		}
		
		documentRepository.deleteByPostId(postId);
	}
}
