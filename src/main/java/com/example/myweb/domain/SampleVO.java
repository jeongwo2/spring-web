package com.example.myweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// part4 JSON 혹은 XML 로 변환될 데이터
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleVO {

  private Integer mno;
  private String  firstName;
  private String  lastName;

}
