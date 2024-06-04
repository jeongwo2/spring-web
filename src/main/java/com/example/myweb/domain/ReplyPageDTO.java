package com.example.myweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

//ex03
@Data
@AllArgsConstructor
@Getter
public class ReplyPageDTO {

  private int replyCnt;
  private List<ReplyVO> list;
}
