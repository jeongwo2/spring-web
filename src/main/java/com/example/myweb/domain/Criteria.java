package com.example.myweb.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//ex02
@ToString
@Setter
@Getter
public class Criteria {

  private int pageNum; // 현재페이지
  private int amount;  // 페이지 당 로우수
  
  private String type;  // 검색조건
  private String keyword; // 검색 키워드

  public Criteria() {
    this(1, 10);
  }

  public Criteria(int pageNum, int amount) {
    this.pageNum = pageNum;
    this.amount = amount;
  }
  
  public String[] getTypeArr() {
    
    return type == null? new String[] {}: type.split("");
  }
}
