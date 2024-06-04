package com.example.myweb.domain;

import lombok.Data;

import java.util.Date;

//ex03
@Data
public class ReplyVO {

  private Long rno;
  private Long bno;

  private String reply;
  private String replyer;
  private Date replyDate;
  private Date updateDate;

}
