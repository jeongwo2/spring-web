package com.example.myweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.myweb.domain.Criteria;
import com.example.myweb.domain.ReplyPageDTO;
import com.example.myweb.domain.ReplyVO;
import com.example.myweb.service.ReplyService;

/**
 * Controller for handling reply-related operations.
 *
 * @author YourName
 * @since 2023-01-01
 */
@RequestMapping("/replies/")
@RestController
@AllArgsConstructor
public class ReplyController {

    /**
     * Logger instance for logging.
     */
    private static final Logger log = LogManager.getLogger(ReplyController.class);

    /**
     * Service for reply-related operations.
     */
    private ReplyService replyService;

    /**[댓글등록]
     * Registers a new reply.
     *
     * @param reply The reply to be registered.
     * @return ResponseEntity with "success" if registration is successful, otherwise returns an error status.
     */
    @PostMapping(value = "/new", consumes = "application/json", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> create(@RequestBody ReplyVO reply) {

        log.info("/new [댓글등록] reply {} ", reply);

        // Count of rows inserted into the database
        int insertCount = replyService.register(reply);
        log.info("Count of rows inserted: " + insertCount);

        // Return success response if exactly one row was inserted
        // Otherwise, return an error response
        return insertCount == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**[댓글검색]
     * Retrieves a reply by its unique identifier.
     *
     * @param rno The unique identifier of the reply.
     * @return ResponseEntity with the reply if found, otherwise returns an error status.
     */
    @GetMapping(value = "/{rno}", produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno) {

        log.info("고유 식별자로 댓글을 검색 rno{} ", rno);
        // 찾은 경우 ResponseEntity 로 댓글을 반환하고, 그렇지 않으면 오류 상태를 반환
        return new ResponseEntity<>(replyService.get(rno), HttpStatus.OK);
    }

    /**[댓글수정]
     * Modifies an existing reply.
     *
     * @param reply The modified reply.
     * @param rno The unique identifier of the reply.
     * @return ResponseEntity with "success" if modification is successful, otherwise returns an error status.
     */
    @RequestMapping(method = { RequestMethod.PUT,
            RequestMethod.PATCH }, value = "/{rno}", consumes = "application/json", produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> modify(
            @RequestBody ReplyVO reply,
            @PathVariable("rno") Long rno) {

        log.info("고유 식별자로 댓글을 수정 rno{} ", rno);
        reply.setRno(rno);

        log.info("[댓글수정] {}", reply);

        return replyService.modify(reply) == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**[댓글삭제]
     * Removes a reply by its unique identifier.
     *
     * @param rno The unique identifier of the reply.
     * @return ResponseEntity with "success" if removal is successful, otherwise returns an error status.
     */
    @DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("고유 식별자로 댓글을 삭제 rno{} ", rno);

        return replyService.remove(rno) == 1 ? new ResponseEntity<>("success", HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**[페이징하여 댓글 검색 ]
     * Retrieves a list of replies for a specific board post, paginated.
     *
     * @param page The page number.
     * @param bno The unique identifier of the board post.
     * @return ResponseEntity with the paginated list of replies if found, otherwise returns an error status.
     */
    @GetMapping(value = "/pages/{bno}/{page}", produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno) {

        log.info("고유 식별자로 페이징하여 댓글 검색 page{}, bno{} ", page, bno);

        Criteria cri = new Criteria(page, 10);

        log.info("페이징 Criteria{} ", cri);

        return new ResponseEntity<>(replyService.getListPage(cri, bno), HttpStatus.OK);
    }
}
