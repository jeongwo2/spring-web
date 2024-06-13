package com.example.myweb.service;


import com.example.myweb.mapper.BoardMapper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myweb.domain.Criteria;
import com.example.myweb.domain.ReplyPageDTO;
import com.example.myweb.domain.ReplyVO;
import com.example.myweb.mapper.ReplyMapper;

import java.util.List;

/** part4 댓글
 * This class implements the ReplyService interface and provides methods for managing reply data.
 * It uses the ReplyMapper interface to interact with the database.
 *
 * @author
 * @since 2023-01-01 ex03
 */
@Service
@AllArgsConstructor
public class ReplyServiceImpl implements ReplyService {

  private static final Logger log = LogManager.getLogger(ReplyServiceImpl.class);
  /**
   * The ReplyMapper interface for database operations.
   */
  private ReplyMapper replyMapper;
  // part5
  private BoardMapper boardMapper;

  /**
   * Registers a new reply in the database.
   *
   * @param vo The ReplyVO object containing the reply data.
   * @return The number of rows affected by the insert operation.
   */
  @Transactional // part5 트랙잭션 추가
  @Override
  public int register(ReplyVO vo) {
    log.info("register.....vo:{}" , vo);
    // part5 댓글과 트랙잭션 설정 추가
    boardMapper.updateReplyCnt(vo.getBno(), 1);

    return replyMapper.insert(vo);
  }

  /**
   * Retrieves a reply from the database by its unique identifier.
   *
   * @param rno The unique identifier of the reply.
   * @return The ReplyVO object representing the reply.
   */
  @Override
  public ReplyVO get(Long rno) {
    log.info("get......" + rno);
    return replyMapper.read(rno);
  }

  /**
   * Modifies an existing reply in the database.
   *
   * @param vo The ReplyVO object containing the updated reply data.
   * @return The number of rows affected by the update operation.
   */
  @Transactional // part5 트랙잭션 추가
  @Override
  public int modify(ReplyVO vo) {
    log.info("modify...vo:{}" , vo);
    return replyMapper.update(vo);
  }

  /** ex03
   * Removes a reply from the database by its unique identifier.
   *
   * @param rno The unique identifier of the reply.
   * @return The number of rows affected by the delete operation.
   */
  @Transactional // part5
  @Override
  public int remove(Long rno) {
    log.info("remove...rno:{}" , rno);
    // part5 댓글과 트랙잭션 설정 추가
    ReplyVO vo = replyMapper.read(rno);
    log.info("remove...vo:{}" , vo);
    boardMapper.updateReplyCnt(vo.getBno(), -1);
    return replyMapper.delete(rno);
  }

  /**
   * Retrieves a list of replies for a specific board, based on the given criteria.
   *
   * @param cri The Criteria object containing the pagination and sorting parameters.
   * @param bno The unique identifier of the board.
   * @return A list of ReplyVO objects representing the replies.
   */
  @Override
  public List<ReplyVO> getList(Criteria cri, Long bno) {
    log.info("get Reply List of a Board " + bno);
    return replyMapper.getListWithPaging(cri, bno);
  }

  /** part4 댓글의 숫자와 댓글 목록을 처리
   * Retrieves a paged list of replies for a specific board, based on the given criteria.
   *
   * @param cri The Criteria object containing the pagination and sorting parameters.
   * @param bno The unique identifier of the board.
   * @return A ReplyPageDTO object containing the total count of replies and the list of ReplyVO objects.
   */
  @Override
  public ReplyPageDTO getListPage(Criteria cri, Long bno) {
    return new ReplyPageDTO(
        replyMapper.getCountByBno(bno),
        replyMapper.getListWithPaging(cri, bno));
  }
}
