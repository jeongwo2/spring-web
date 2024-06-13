package com.example.myweb.mapper;

import com.example.myweb.domain.Criteria;
import com.example.myweb.domain.ReplyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** part4 Ajax 를 이용하는 댓글 처리
 * This interface provides methods for interacting with the ReplyVO objects in the database.
 * It is used by MyBatis to map SQL queries to method calls.
 *
 * @author
 * @since 2023-01-01 ex03
 */
public interface ReplyMapper {

    public int insert(ReplyVO vo);
    // 댓글의 번호를 이용해서 특정 댓글 조회
    public ReplyVO read(Long bno);

    public int delete(Long bno);
    // 특정 댓글 수정
    public int update(ReplyVO reply);

    /**
     * Retrieves a list of ReplyVO objects from the database based on the given criteria and bno.
     * MyBatis 의 파라미터는 1개만 허용
     * @param cri The criteria object containing pagination and sorting information.
     * @param bno The unique identifier of the ReplyVO objects to be retrieved.
     * @return A list of ReplyVO objects that match the given criteria and bno.
     */
    public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Long bno);

    public int getCountByBno(Long bno);
}
