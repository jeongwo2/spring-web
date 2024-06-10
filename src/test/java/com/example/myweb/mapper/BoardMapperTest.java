package com.example.myweb.mapper;

import com.example.myweb.domain.BoardVO;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class BoardMapperTest {

    @Setter(onMethod = @__({ @Autowired}))
    private BoardMapper boardMapper;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetList() {
        boardMapper.getList().forEach(board -> log.info(board));
    }

    @Test
    public void testInsert() {

        BoardVO board = new BoardVO();
        board.setTitle("새로 작성하는 글");
        board.setContent("새로 작성하는 내용");
        board.setWriter("newbie");

        boardMapper.insert(board);

        log.info(board);
    }

    @Test
    public void testRead() {
        // 존재하는 게시물 번호로 테스트
        BoardVO board = boardMapper.read(5L);
        log.info(board);
    }

}