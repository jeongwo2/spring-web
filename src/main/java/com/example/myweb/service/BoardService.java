package com.example.myweb.service;

import com.example.myweb.domain.BoardAttachVO;
import com.example.myweb.domain.BoardVO;
import com.example.myweb.domain.Criteria;

import java.util.List;

//ex02
public interface BoardService {

	public void register(BoardVO board);

	public BoardVO get(Long bno);

	public boolean modify(BoardVO board);

	public boolean remove(Long bno);

	public List<BoardVO> getList();

	public List<BoardVO> getListWithPaging(Criteria cri);

	//추가
	public int getTotal(Criteria cri);
    // 검색조건
	public List<BoardVO> getListWithSearch(Criteria cri);
    //ex05
	public List<BoardAttachVO> getAttachList(Long bno);
}
