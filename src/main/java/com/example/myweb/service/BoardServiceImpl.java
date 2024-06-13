package com.example.myweb.service;

import com.example.myweb.domain.BoardAttachVO;
import com.example.myweb.domain.BoardVO;
import com.example.myweb.domain.Criteria;
import com.example.myweb.mapper.BoardAttachMapper;
import com.example.myweb.mapper.BoardMapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Log4j2
/**ex02
 * BoardServiceImpl class is the implementation of BoardService interface.
 * It provides methods for interacting with the board data.
 *
 * @author YourName
 * @since 2023-01-01
 */
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	/**
	 * Logger instance for logging messages.
	 */
	private static final Logger log = LogManager.getLogger(BoardServiceImpl.class);

	/**
	 * BoardMapper instance for database operations.
	 */
	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;

	// ex04
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;

	/**
	 * Registers a new board in the database.
	 *
	 * @param board The board object to be registered.
	 * @throws RuntimeException If any error occurs during the database operation.
	 */
	@Transactional // Part6
	@Override
	public void register(BoardVO board) {

		log.info("register......{}", board);

		boardMapper.insertSelectKey(board);
		// Part4
		if (board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		// Part6 트랜잭션하에 여러 개의 첨부 파일 정보도 DB에 저장
		board.getAttachList().forEach(attach -> {
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	}

	/**
	 * Retrieves a board from the database by its unique identifier.
	 *
	 * @param bno The unique identifier of the board.
	 * @return The board object if found, otherwise null.
	 */
	@Override
	public BoardVO get(Long bno) {

		log.info("get......by bno{}", bno);

		return boardMapper.read(bno);
	}

	/**
	 * Modifies an existing board in the database.
	 *
	 * @param board The board object with updated information.
	 * @return True if the modification is successful, otherwise false.
	 * @throws RuntimeException If any error occurs during the database operation.
	 */
	@Transactional // ex03
	@Override
	public boolean modify(BoardVO board) {

		log.info("modify......by board{}", board);
		//ex04
		attachMapper.deleteAll(board.getBno());

		boolean modifyResult = boardMapper.update(board) == 1;
		if (modifyResult && board.getAttachList().size() > 0) {

			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		return modifyResult;
	}

	/**
	 * Removes a board from the database by its unique identifier.
	 *
	 * @param bno The unique identifier of the board.
	 * @return True if the removal is successful, otherwise false.
	 * @throws RuntimeException If any error occurs during the database operation.
	 */
	@Transactional // ex03
	@Override
	public boolean remove(Long bno) {

		log.info("remove....by bno{}", bno);
		//ex04
		attachMapper.deleteAll(bno);

		return boardMapper.delete(bno) == 1;
	}

	/**
	 * Retrieves a list of all boards from the database.
	 *
	 * @return A list of board objects.
	 */
	@Override
	public List<BoardVO> getList() {

		log.info("getList..........");

		return boardMapper.getList();
	}

	public List<BoardVO> getList(Criteria cri) {

		log.info("get List with criteria: " + cri);

		return boardMapper.getListWithPaging(cri);
	}

	/**
	 * Retrieves the total count of boards in the database.
	 *
	 * @param cri The criteria object containing pagination and sorting information.
	 * @return The total count of boards.
	 */
	@Override
	public int getTotal(Criteria cri) {

		log.info("get total count");
		return boardMapper.getTotalCount(cri);
	}

	/**
	 * Retrieves a list of boards from the database based on pagination and sorting criteria.
	 *
	 * @param cri The criteria object containing pagination and sorting information.
	 * @return A list of board objects that match the criteria.
	 */
	@Override
	public List<BoardVO> getListWithPaging(Criteria cri) {
		log.info("아이템 목록을 검색 criteria{}", cri);

		return boardMapper.getListWithPaging(cri);
	}


	/**
	 * Retrieves a list of boards from the database based on a search keyword.
	 *
	 * @param cri The criteria object containing the search keyword.
	 * @return A list of board objects that match the search keyword.
	 */
	@Override
	public List<BoardVO> getListWithSearch(Criteria cri) {
		log.info("get List with search keyword{}}", cri);
		return boardMapper.getListWithSearch(cri);
	}
    //ex05
	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {

		log.info("get Attach list by bno" + bno);

		return attachMapper.findByBno(bno);
	}

}
