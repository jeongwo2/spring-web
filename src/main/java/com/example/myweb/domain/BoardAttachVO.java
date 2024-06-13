package com.example.myweb.domain;

import lombok.Data;

// Part3
@Data
public class BoardAttachVO {
  private String uuid;
  private String uploadPath;
  private String fileName;
  private boolean fileType;
  // Part6 파일 정보들을 BoardAttachVO로 변환
  private Long bno;
}
