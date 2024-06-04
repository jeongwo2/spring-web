package com.example.myweb.service;

import org.springframework.stereotype.Service;

/*
 * ex04: AOP 공통적인 기능(예: 로깅, 트랜잭션 관리)이 코드를 재 사용
 * 인터페이스를 구현하고, 두 개의 문자열 매개변수를 받아서 정수로 변환하여 더한 후, 그 결과를 반환합니다.
 */
@Service
public class SampleServiceImpl implements SampleService {

	@Override
	public Integer doAdd(String str1, String str2) throws Exception {

		return Integer.parseInt(str1) + Integer.parseInt(str2);

	}

}


