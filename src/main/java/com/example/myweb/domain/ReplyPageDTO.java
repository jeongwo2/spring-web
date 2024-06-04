package com.example.myweb.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

//ex03
@Data
@AllArgsConstructor
@Getter
public class ReplyPageDTO {

  private int replyCnt;
  private List<ReplyVO> list;
}
