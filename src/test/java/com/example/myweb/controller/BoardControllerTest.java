package com.example.myweb.controller;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.*;

@Log4j2
@RunWith(SpringJUnit4ClassRunner.class)
//Test for Controller
@WebAppConfiguration
@ContextConfiguration({
        "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
public class BoardControllerTest {
    /*
     * Spring IoC 컨테이너가 ctx 필드에 WebApplicationContext 타입의 객체를 자동으로 주입
     */
    @Setter(onMethod_ = {@Autowired} )
    private WebApplicationContext ctx;
    /*
     * Spring MVC 테스트를 위해 HTTP 요청을 만들고 응답을 검증할 수 있는 객체
     */
    private MockMvc mockMvc;

    /**
     * This method is used to set up the MockMvc object for testing the controller.
     * It initializes the MockMvc object with the WebApplicationContext ctx.
     * The MockMvc object is used to perform HTTP requests and verify the responses.
     *
     * @throws Exception if an error occurs during setup
     */
    @Before
    public void setUp() throws Exception {
        // Use MockMvcBuilders to create a MockMvc instance that is configured with the WebApplicationContext ctx
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build(); //  Spring MVC application's context initialization
    }

    /**
     * This method is used to test the list functionality of the BoardController.
     * It sends a GET request to "/board/list" endpoint and retrieves the ModelAndView object.
     * The ModelAndView object contains the model data and the view name.
     * The method then logs the model data using the log4j2 logger.
     *
     * @throws Exception if an error occurs during the HTTP request or response handling
     */
    @Test
    public void testList() throws Exception {
        // Send a GET request to "/board/list" endpoint
        // Perform the request and retrieve the result
        // Get the ModelAndView object from the result
        ModelAndView mav = mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
                                 .andReturn()
                                 .getModelAndView();

        // Check if the ModelAndView object is not null
        if (mav!= null) {
            // Log the model data using the log4j2 logger
            log.info(mav.getModelMap());
        }
    }

    @Test
    public void testRegister() throws Exception {
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
                .param("title", "테스트 새글 제목")
                .param("content", "테스트 새글 내용")
                .param("writer", "user00")
        ).andReturn().getModelAndView().getViewName();

        if (resultPage!= null) {
            log.info(resultPage);
        }
    }

    @Test
    public void toGet() throws Exception {
        ModelAndView mav = mockMvc.perform(MockMvcRequestBuilders
                                      .get("/board/get")
                                      .param("bno", "2"))
                                  .andReturn()
                                  .getModelAndView();
        if (mav!= null) {
            log.info(mav.getModelMap());
        }

    }

    @Test
    public void testModify() throws Exception {
        String resultPage = mockMvc
                .perform(MockMvcRequestBuilders.post("/board/modify")
                        .param("bno", "1")
                        .param("title", "수정된 테스트 새글 제목")
                        .param("content", "수정된 테스트 새글 내용")
                        .param("writer", "user00"))
                .andReturn().getModelAndView().getViewName();

        if (resultPage!= null) {
            log.info(resultPage);
        }

    }

    @Test
    public void testRemove() throws Exception {
        //삭제전 데이터베이스에 게시물 번호 확인할 것
        String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/remove")
                                        .param("bno", "25")
                                           ).andReturn()
                                    .getModelAndView().getViewName();

        if (resultPage!= null) {
            log.info(resultPage);
        }
    }
}