package com.example.myweb.controller;

import com.example.myweb.domain.SampleVO;
import com.example.myweb.domain.Ticket;
import com.example.myweb.dto.SampleDTO;
import com.example.myweb.dto.SampleDTOList;
import com.example.myweb.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Part2, Part4
@RestController // Part4
@RequestMapping("/sample/*")
@Log4j2
public class SampleController {

    @RequestMapping("")
    public void basic() {
        log.info("basic...................");
    }
    @RequestMapping(value = "/basic", method = { RequestMethod.GET, RequestMethod.POST })
    public void basicGet() {
        log.info("basic get...................");
    }
    @GetMapping("/ex01")
    public String ex01(SampleDTO dto) {
        log.info("" + dto);
        return "ex01";
    }
    @GetMapping("/ex02")
    public String ex02(@RequestParam("name") String name, @RequestParam("age") int age) {
        log.info("name: " + name);
        log.info("age: " + age);
        return "ex02";
    }
    @GetMapping("/ex02List")
    public String ex02List(@RequestParam("ids") ArrayList<String> ids) {

        log.info("ids: " + ids);

        return "ex02List";
    }

    @GetMapping("/ex02Array")
    public String ex02Array(@RequestParam("ids") String[] ids) {

        log.info("array ids: " + Arrays.toString(ids));

        return "ex02Array";
    }

    @GetMapping("/ex02Bean")
    public String ex02Bean(SampleDTOList list) {

        log.info("list dtos: " + list);

        return "ex02Bean";
    }

    @GetMapping("/ex03")
    public String ex03(TodoDTO todo) {
        log.info("todo: " + todo);
        return "ex02";
    }

    @GetMapping("/ex04")
    public String ex04(SampleDTO dto, @ModelAttribute("page") int page) {

        log.info("dto: " + dto);
        log.info("page: " + page);

        return "/sample/ex04";
    }

    @GetMapping("/ex05")
    public void ex05() {
        log.info("/ex05..........");
    }

    @GetMapping("/ex06")
    public @ResponseBody SampleDTO ex06() {
        log.info("/ex06..........");

        SampleDTO dto = new SampleDTO();
        dto.setAge(10);
        dto.setName("홍길동");

        return dto;
    }

    @GetMapping("/ex07")
    public ResponseEntity<String> ex07() {
        log.info("/ex07..........");

        // {"name": "홍길동"}
        String msg = "{\"name\": \"홍길동\"}";

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>(msg, header, HttpStatus.OK);
    }

    @GetMapping("/exUpload")
    public void exUpload() {
        log.info("/exUpload..........");
    }

    @PostMapping("/exUploadPost")
    public void exUploadPost(ArrayList<MultipartFile> files) {
        log.info("/exUploadPost..........");
        files.forEach(file -> {
            log.info("name:" + file.getOriginalFilename());
            log.info("size:" + file.getSize());
        });
        log.info("----------------------------------");
    }

    // Part7 시큐리티 적용이 필요한 URI 설계
    // http://localhost:8081/sample/all
    @GetMapping("/all")
    public void doAll() {

        log.info("do all can access everybody{}", "로그인을 하지 않은 사용자도 접근 가능한 URI");
    }

    @GetMapping("/member")
    public void doMember() {

        log.info("logined member {}", "로그인 한 사용자들만이 접근할 수 있는 URI");
    }

    @GetMapping("/admin")
    public void doAdmin() {

        log.info("admin only {}", "관리자 권한을 가진 사용자만이 접근");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MEMBER')")
    @GetMapping("/annoMember")
    public void doMember2() {

        log.info("logined annotation member");
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/annoAdmin")
    public void doAdmin2() {

        log.info("admin Annotaion only");
    }
    // part4
    @GetMapping(value = "/getSample", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public SampleVO getSample() {

        return new SampleVO(112, "스타", "로드");
    }

    /**
     * Java 8의 Stream API 를 사용하여 SampleVO 객체의 목록을 생성 메서드
     * 이 메서드는 Java 8의 IntStream 을 사용하여 1에서 9까지의 숫자를 생성합니다.
     * 그런 다음, mapToObj 를 사용하여 각 숫자를 SampleVO 객체로 변환합니다.
     * 마지막으로, collect 를 사용하여 SampleVO 객체의 목록을 생성합니다.
     *
     * @return a list of SampleVO objects
     */
    @GetMapping(value = "/getList")
    public List<SampleVO> getList() {

        // Use IntStream to generate a sequence of numbers from 1 to 9
        // Then use mapToObj to convert each number to a SampleVO object
        // Finally, collect the resulting stream into a list
        return IntStream.range(1, 10).mapToObj(i -> new SampleVO(i, i + "First", i + " Last"))
               .collect(Collectors.toList());
    }
    @GetMapping(value = "/getMap")
    public Map<String, SampleVO> getMap() {

        Map<String, SampleVO> map = new HashMap<>();
        map.put("First", new SampleVO(111, "그루트", "주니어"));

        return map;
    }

    /**
     * height 와 weight 매개변수를 확인하고 ResponseEntity<SampleVO> 객체를 반환하는 메서드
     *
     * @param height 확인할 키 값
     * @param weight 확인할 몸무게 값
     * @return ResponseEntity<SampleVO> 상태와 본문을 포함하는 ResponseEntity 객체
     *         height가 150 미만이면 상태는 BAD_GATEWAY이고 본문에는 height와 weight이 포함된 SampleVO 객체가 포함됩니다.
     *         height가 150 이상이면 상태는 OK이고 본문에는 height와 weight이 포함된 SampleVO 객체가 포함됩니다.
     */
    @GetMapping(value = "/check", params = { "height", "weight" })
    public ResponseEntity<SampleVO> check(Double height, Double weight) {

        // Create a SampleVO object with the provided height and weight
        SampleVO vo = new SampleVO(000, "" + height, "" + weight);

        ResponseEntity<SampleVO> result = null;

        // Check if the height is less than 150
        if (height < 150) {
            // If true, set the status to BAD_GATEWAY and the body to the SampleVO object
            result = ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
        } else {
            // If false, set the status to OK and the body to the SampleVO object
            result = ResponseEntity.status(HttpStatus.OK).body(vo);
        }
        // Return the ResponseEntity object
        return result;
    }

    /* This method is used to handle HTTP GET requests to the "/product/{cat}/{pid}" endpoint.
     *  @PathVariable: URL 경로의 일부를 파라미터로 사용할 때 이용
     * Example usage: http://localhost:8080/sample/product/cat/123
     * this method will return ["category: cat", "productid: 123"]
     */
    @GetMapping("/product/{cat}/{pid}")
    public String[] getPath(
            @PathVariable("cat") String cat,
            @PathVariable("pid") Integer pid) {

        return new String[] { "category: " + cat, "productid: " + pid };
    }
    /*
     * @RequestBody: 들어오는 JSON 요청 본문을 Ticket 객체에 바인딩합니다.
     *  JSON 데이터는 자동으로 Ticket 클래스에 정의된 속성을 기반으로 Ticket 객체로 변환됩니다.
     * @param ticket The Ticket object that will be populated with the data from the incoming JSON request body.
     * @return The Ticket object that was populated with the data from the incoming JSON request body.
     *
     * @throws IllegalArgumentException If the incoming JSON request body does not match the expected format or structure.
     * @throws IllegalStateException If there is an error during the conversion process.
     */
    @PostMapping("/ticket") // HTTP POST 요청을 처리
    public Ticket convert(@RequestBody Ticket ticket) {

        log.info("convert.......ticket{}", ticket);

        return ticket;
    }
}
