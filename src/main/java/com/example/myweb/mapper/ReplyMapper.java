package com.example.myweb.mapper;

import com.example.myweb.domain.Criteria;
import com.example.myweb.domain.ReplyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * This interface provides methods for interacting with the ReplyVO objects in the database.
 * It is used by MyBatis to map SQL queries to method calls.
 *
 * @author
 * @since 2023-01-01 ex03
 */
public interface ReplyMapper {

    public int insert(ReplyVO vo);

    public ReplyVO read(Long bno);

    public int delete(Long bno);

    public int update(ReplyVO reply);

    /**
     * Retrieves a list of ReplyVO objects from the database based on the given criteria and bno.
     *
     * @param cri The criteria object containing pagination and sorting information.
     * @param bno The unique identifier of the ReplyVO objects to be retrieved.
     * @return A list of ReplyVO objects that match the given criteria and bno.
     */
    public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri, @Param("bno") Long bno);

    public int getCountByBno(Long bno);
}
