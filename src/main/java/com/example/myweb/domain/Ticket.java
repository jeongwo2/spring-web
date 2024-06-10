package com.example.myweb.domain;

import lombok.Data;

// part4 전송된 데이터가 JSON 이고, 이를 컨트롤러에서는 사용자 정의 타입의 객체로 변환할때 사용
@Data
public class Ticket {

    private int tno;
    private String owner;
    private String grade;
}

