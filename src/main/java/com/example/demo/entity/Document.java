package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("DMT")
public class Document extends FileBean{
	@Column(name="size")
	private Long fileSize;
	
	@Column(name="post_id")
	private Long postId;
	
	private String uuId;
	private String fileName;
	private String contentType;
}