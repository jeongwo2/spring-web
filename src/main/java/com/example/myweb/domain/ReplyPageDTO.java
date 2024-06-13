package com.example.myweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

//part4 댓글의 숫자와 댓글 목록을 처리하는 VO
@Data
@AllArgsConstructor
@Getter
public class ReplyPageDTO {
  private int replyCnt;
  private List<ReplyVO> list;
}
