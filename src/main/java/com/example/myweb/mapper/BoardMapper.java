package com.example.myweb.mapper;

import java.util.List;

import com.example.myweb.domain.BoardVO;
import com.example.myweb.domain.Criteria;

//ex02
public interface BoardMapper {

	// 전체조회
	public List<BoardVO> getList();

	// 페이징
	public List<BoardVO> getListWithPaging(Criteria cri);

	// 검색조건
	public List<BoardVO> getListWithSearch(Criteria cri);

	public void insert(BoardVO board);
    // Oracle 시퀀스 채번 후 Insert
	public Integer insertSelectKey(BoardVO board);

	public BoardVO read(Long bno);

	public int delete(Long bno);

	public int update(BoardVO board);

	// 전제건수
	public int getTotalCount(Criteria cri);
}
