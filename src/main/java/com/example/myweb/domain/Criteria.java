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
  private int pagePerNum;  // 페이지 당 로우수 amount 를 rename to pagePerNum
  // part3 검색조건처리를 위한
  private String type;  // 검색조건
  private String keyword; // 검색 키워드

  public Criteria() {
    this(1, 10);
  }

  public Criteria(int pageNum, int pagePerNum) {
    this.pageNum = pageNum;

    this.pagePerNum = pagePerNum;
  }
  
  public String[] getTypeArr() {
    
    return type == null? new String[] {}: type.split("");
  }
  // part3 검색후 조회,수정,삭제 페이지 이동시 검색 항목 유지

}
