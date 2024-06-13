package com.example.myweb.domain;

import lombok.Data;
import java.util.List;
import java.util.Date;

//ex02
@Data
public class BoardVO {

  private Long bno;   // oracle number
  private String title;
  private String content;
  private String writer;
  private Date regdate;
  private Date updateDate;

  // Part6 여러 개의 첨부 파일을 가지도록
  private List<BoardAttachVO> attachList;
}
