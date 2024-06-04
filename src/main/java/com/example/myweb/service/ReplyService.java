package com.example.myweb.service;

import com.example.myweb.domain.Criteria;
import com.example.myweb.domain.ReplyPageDTO;
import com.example.myweb.domain.ReplyVO;

import java.util.List;
/**
 * This interface defines the contract for the Reply Service.
 * It provides methods for managing replies in a web application.
 *
 * @author YourName
 * @since 2023-01-01 ex03
 */
//
public interface ReplyService {

	public int register(ReplyVO vo);

	public ReplyVO get(Long rno);

	public int modify(ReplyVO vo);

	public int remove(Long rno);

	public List<ReplyVO> getList(Criteria cri, Long bno);
	
	public ReplyPageDTO getListPage(Criteria cri, Long bno);

}
