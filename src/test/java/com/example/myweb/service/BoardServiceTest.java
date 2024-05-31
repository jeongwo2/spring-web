package com.example.myweb.service;

import com.example.myweb.domain.BoardVO;
import com.example.myweb.domain.Criteria;
import com.example.myweb.mapper.BoardMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class BoardServiceTest {

    @Setter(onMethod_ = { @Autowired})
    private BoardService service;

    @Setter(onMethod_ = @Autowired)
    private BoardMapper boardMapper;

    @Test
    public void testExist() {

        log.info(service);
        assertNotNull(service);
    }

    @Test
    public void testGetList() {

        service.getList().forEach(board -> log.info(board));
    }

    @Test
    public void testRegister() {

        BoardVO board = new BoardVO();
        board.setTitle("새글");
        board.setContent("키 중복 시 insertSelectKey");
        board.setWriter("user1");

        Long bno = 0L;
        try{
            service.register(board);

            bno = board.getBno();

        }catch (Exception e)
        {
            log.error(e.getMessage());
        }
        log.info("생성된 게시물의 번호: " + bno);
    }

    @Test
    public void testInsertSelectKey() {

        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글");
        board.setContent("새로 작성하는 내용");
        board.setWriter("user2");

        boardMapper.insertSelectKey(board);

        Long bno = 0L;
        bno = board.getBno();
        if ( bno > 1 )
        {
            log.info("생성된 게시물의 번호: " + bno);
        }
    }


    @Test
    public void testGetListWithPaging() {

        service.getListWithPaging(new Criteria(2, 10)).forEach(board -> log.info(board));
    }

    @Test
    public void testGet() {

        log.info(service.get(1L));
    }

    @Test
    public void testDelete() {
        // 게시물 번호의 존재 여부를 확인하고 테스트할 것
        log.info("REMOVE RESULT: " + service.remove(2L));
    }

    @Test
    public void testUpdate() {

        BoardVO board = service.get(1L);

        if (board == null) {
            return;
        }

        board.setTitle("제목 수정");
        board.setContent("제목 수정합니다.");
        service.modify(board);

        board = service.get(1L);

        log.info("MODIFY RESULT: " + board);
    }
}