package com.example.myweb.domain;

import lombok.Data;

import java.util.Date;

//part4 Ajax 를 이용하는 댓글 처리
@Data
public class ReplyVO {

  private Long rno;
  private Long bno;

  private String reply;
  private String replyer;
  private Date replyDate;
  private Date updateDate;

}
