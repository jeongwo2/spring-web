package com.example.myweb.mapper;

import com.example.myweb.domain.BoardVO;
import com.example.myweb.domain.Criteria;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//ex02
public interface BoardMapper {

	// 전체조회
	//@Select("select * from tbl_board where bno > 0")
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

	// part3 전제건수
	public int getTotalCount(Criteria cri);
}
