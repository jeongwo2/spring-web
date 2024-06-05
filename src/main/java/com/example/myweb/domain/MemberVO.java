package com.example.myweb.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

// ex06
@Data
public class MemberVO {

	private String userid;
	private String userpw;
	private String userName;
	private boolean enabled;

	private Date regDate;
	private Date updateDate;
	private List<AuthVO> authList;

}
