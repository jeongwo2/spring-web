package com.example.myweb.mapper;

import com.example.myweb.domain.BoardAttachVO;

import java.util.List;

/** ex05: 첨부파일
 */
public interface BoardAttachMapper {

	public void insert(BoardAttachVO vo);

	public void delete(String uuid);

	public List<BoardAttachVO> findByBno(Long bno);

	public void deleteAll(Long bno);

	public List<BoardAttachVO> getOldFiles();

}