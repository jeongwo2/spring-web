package com.example.myweb.domain;

import lombok.Data;

@Data
public class BoardAttachVO {

  private String uuid;
  private String uploadPath;
  private String fileName;
  private boolean fileType;
  // ex05
  private Long bno;
  
}
