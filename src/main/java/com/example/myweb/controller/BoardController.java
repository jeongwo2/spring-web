package com.example.myweb.controller;

import com.example.myweb.domain.BoardVO;
import com.example.myweb.domain.Criteria;
import com.example.myweb.domain.PageDTO;
import com.example.myweb.service.BoardService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//ex02
@Controller
//@Log4j2
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private static final Logger log = LogManager.getLogger(BoardController.class);
	/*
	 * DI 적용 시, BoardService 타입 : 1. BoardService 인터페이스 2. BoardServiceImpl 클래스
	 * BoardService 타입으로 선언했지만 자동으로 BoardServiceImpl 클래스가 주입되었다.
	 */
	private BoardService boardService;

	@GetMapping("/register")
	public void register() {

	}

	// @GetMapping("/list")
	// public void list(Model model) {	//
	// log.info("list");
	// model.addAttribute("list", service.getList());	//
	// }

	// @GetMapping("/list")
	// public void list(Criteria cri, Model model) {	//
	// log.info("list: " + cri);
	// model.addAttribute("list", service.getListWithPaging(cri));
	// }

    /*
     * [목록] 페이지 이동: http://localhost:8081/board/list
     */
	@GetMapping("/list") // 엔드포인트에 대한 HTTP GET 요청
	public void list(Criteria cri, Model model) {
		log.info("/list ==>[목록] Criteria cri 객체를 로깅{}", cri);

		model.addAttribute("list", boardService.getListWithPaging(cri));
		// model.addAttribute("pageMaker", new PageDTO(cri, 123));

		int total = boardService.getTotal(cri);
		log.info("총 아이템 count{}: " , total);

		log.info("PageDTO 객체를 생성-Paging 처리");
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
    /* [글 등록]: http://localhost:8081/board/register
	 * @param board The board object to be registered.
	 * @param rttr  The RedirectAttributes object to store flash attributes for redirecting.
	 * @return A string representing the redirect URL to the board list page.
	 */
	@PostMapping("/register") // 엔드포인트에 대한 HTTP POST 요청을 처리
	public String register(BoardVO board, RedirectAttributes rttr) {

		log.info("/register ==>[등록]board:{} " , board.getBno());
		// Register the board using the boardService
		boardService.register(board);

		log.info("단 한번만 전송되는 데이터 저장후 전송 bNo:{} " , board.getBno());
		rttr.addFlashAttribute("result", board.getBno());

		log.info("Redirect to the board list page=>/board/list");
		return "redirect:/board/list"; // 모달창
	}

	// @GetMapping({ "/get", "/modify" })
	// public void get(@RequestParam("bno") Long bno, Model model) {
	// log.info("/get or modify ");
	// model.addAttribute("board", service.get(bno));
	// }

	/**
	 * [검색] [수정]
	 *
	 * @param bno The unique identifier of the board item to retrieve or modify.
	 * @param cri The Criteria object containing pagination and search parameters.
	 * @param model The Model object to store data for rendering in the view.
	 *
	 * @return void (as this method is used for rendering a view, not returning a response)
	 */
	@GetMapping({ "/get", "/modify" })
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {

		log.info("/get or modify cri{}", cri);
		// Retrieve the board item with the given bno from the boardService and add it to the model
		log.info("검색된 게시판 항목을 model 에 추가 bNo {}" , bno);
		model.addAttribute("board", boardService.get(bno));
		// 뷰 렌더링을 위해 사용되므로 값을 반환하지 않음
	}

	// @PostMapping("/modify")
	// public String modify(BoardVO board, RedirectAttributes rttr) {
	// log.info("modify:" + board);
	//
	// if (service.modify(board)) {
	// rttr.addFlashAttribute("result", "success");
	// }
	// return "redirect:/board/list";
	// }

	/** [수정처리] 삭제 후 목록으로 이동
	 * Handles the HTTP POST request for modifying a board item.
	 * @param board 업데이트할 게시판 항목
	 * @param cri pagination and search parameters.
	 * @param rttr 리다이렉트할 때 플래시 속성을 저장
	 * @return A string representing the redirect URL to the board list page.
	 */
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("/modify board{}}" , board);
		// Attempt to modify the board item using the boardService
		if (boardService.modify(board)) {
			log.info("수정이 성공하면 플래시 속성 추가  result{}", "success" );
			rttr.addFlashAttribute("result", "success");
		}
		// Add pagination and search parameters to the redirect URL
		log.info("Add pagination and search parameters cri{}", cri );
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getPagePerNum());
		//part4
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		log.info("Redirect to the board list page =>/board/list");
		return "redirect:/board/list";
	}

	// @PostMapping("/remove")
	// public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr)
	// {
	// log.info("remove..." + bno);
	// if (service.remove(bno)) {
	// rttr.addFlashAttribute("result", "success");
	// }
	// return "redirect:/board/list";
	// }

	/**[삭제]
	 * Handles the HTTP POST request for removing a board item.
	 *
	 * @param bno The unique identifier of the board item to be removed.
	 * @param cri The Criteria object containing pagination and search parameters.
	 * @param rttr The RedirectAttributes object to store flash attributes for redirecting.
	 * @return A string representing the redirect URL to the board list page.
	 */
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, Criteria cri, RedirectAttributes rttr) {

		log.info("/remove...bno{}" , bno);
		// Attempt to remove the board item using the boardService
		if (boardService.remove(bno)) {
			// If the removal is successful, add a flash attribute to the redirect URL for success message
			rttr.addFlashAttribute("result", "success");
		}

		log.info("Add pagination and search parameters  cri{}", cri );
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getPagePerNum());
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		log.info("Redirect to the board list page=>/board/list");
		return "redirect:/board/list";
	}

}
