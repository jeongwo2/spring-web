package com.example.myweb.controller;

import com.example.myweb.domain.SampleVO;
import com.example.myweb.domain.Ticket;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.myweb.service.SampleService;
import com.google.gson.Gson;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/spring/root-context.xml",
        "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@Log4j2
public class SampleControllerTest {
    @Setter(onMethod_ = { @Autowired})
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Setter(onMethod_ = { @Autowired})
    private SampleService sampleService;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }


    @Test
    public void testEx02List() {
        // Arrange
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ids.add("id" + i);
        }
        String expectedLogMessage = "ids: [id0, id1,..., id999]";
        String expectedReturnString = "ex02List";

    }

    @Test
    public void testGetSample() throws Exception {

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/sample/getSample")
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        SampleVO sampleVO = objectMapper.readValue(content, SampleVO.class);
        assertEquals(sampleVO.getMno().intValue(), 112); // Removed Optional.ofNullable()
        assertEquals(sampleVO.getFirstName(), "스타");
        assertEquals(sampleVO.getLastName(), "로드");

    }

    @Test
    public void testConvert() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setTno(123);
        ticket.setOwner("Admin");
        ticket.setGrade("AAA");

        String jsonStr = new Gson().toJson(ticket);

        log.info(jsonStr);

        mockMvc.perform(post("/sample/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr))
                .andExpect(status().is(200));
    }
}