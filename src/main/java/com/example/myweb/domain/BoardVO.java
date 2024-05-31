package com.example.myweb.domain;

import java.util.Date;

import lombok.Data;

//ex02
@Data
public class BoardVO {

  private Long bno;   // oracle number
  private String title;
  private String content;
  private String writer;
  private Date regdate;
  private Date updateDate;
}
