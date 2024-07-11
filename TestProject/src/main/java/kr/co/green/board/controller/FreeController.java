package kr.co.green.board.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.board.model.service.FreeService;
import kr.co.green.board.model.service.FreeServiceImpl;
import kr.co.green.common.paging.PageInfo;
import kr.co.green.common.paging.Pagination;

@Controller
@RequestMapping("/free")
public class FreeController {
	private static final Logger logger = LogManager.getLogger(FreeController.class);
	
	private final FreeServiceImpl freeService;
	private final Pagination pagination;

	@Autowired
	public FreeController(FreeServiceImpl freeService, Pagination pagination) {
		this.freeService = freeService;
		this.pagination = pagination;
	}

	@GetMapping("/list.do")
	public String freeList(Model model, @RequestParam(value = "cpage", defaultValue = "1") int cpage, FreeDto free, HttpSession session, @ModelAttribute("msg") String msg) {
		// RequestParam annotation : 쿼리스트링을 받을때
		// value : 쿼리스트링 키

		logger.info("/free/List 호출 완료 : cpage={}, memberNo={}", 
				cpage, session.getAttribute("memberNo"));
		
		// 1. 전체 게시글 수 구하기(페이징 처리)
		int listCount = freeService.getListCount(free);
		int pageLimit = 5;
		int boardLimit = 5;
		int row = listCount - (cpage - 1) * boardLimit;

		PageInfo pi = pagination.getPageInfo(listCount, cpage, pageLimit, boardLimit);

		List<FreeDto> list = freeService.freeList(pi, free);

		
		model.addAttribute("row", row);
		model.addAttribute("pi", pi);
		model.addAttribute("list", list); // 객체 바인딩
		
//		model.addAttribute("msg", session.getAttribute("msg"));
		logger.debug("호출된 게시글 : list size = {}", list.size());
		
		return "board/free/freeList";

	}

	@GetMapping("/detail.do")
	public String freeDetail(FreeDto free, Model model, HttpSession session, HttpServletRequest request, @RequestParam(value = "boardNo", defaultValue = "null") int boardNo) {
//		String referer = request.getHeader("referer");
//				
//		if(referer == null || !referer.endsWith("/list.do")) {
//			return "common/error";
//		}
		logger.info("상세 조회 요청: boardNo={}, memberNo={}",
			       free.getBoardNo(), session.getAttribute("memberNo"));
		
		BoardDto result = freeService.getDetail(free, "detail");

		// 1. resources 이후의 문자열 가져오기
		if (result.getUploadName() != null && result.getUploadPath() != null) {
			int pathIndex = result.getUploadPath().lastIndexOf("resources");

			String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");

			result.setUploadPath(path);
		}

		if (!Objects.isNull(result)) {
			model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
			model.addAttribute("result", result);
			logger.debug("상세 조회 성공: {}", boardNo);
			return "board/free/freeDetail";
		} else {
			logger.debug("상세 조회 실패: {}", boardNo);
			return "/common/error";
		}
	}

	@GetMapping("/enrollForm.do")
	public String enrollForm(Model model, HttpSession session) {
		model.addAttribute("memberName", session.getAttribute("memberName"));
		return "board/free/freeEnroll";
	}

	@PostMapping("/enroll.do")
	public String setEnroll(BoardDto board, MultipartFile upload, HttpSession session) {
		board.setMemberNo((int) session.getAttribute("memberNo"));
		
		logger.info("[자유게시판] 게시글 등록 요청: memberNo={}",
			       session.getAttribute("memberNo"));

		int result = freeService.setEnroll(board, upload, session);

		if (result == 1) {
			logger.debug("[자유게시판] 게시글 등록 성공: boardNo={}", board.getBoardNo());
			return "redirect:/free/list.do";
		} else {
			logger.debug("[자유게시판] 게시글 등록 실패: boardNo={}", board.getBoardNo());
			return "/common/error";
		}
	}
	
	@GetMapping("/delete.do")
//	public String delete(String boardNo, int memberNo, HttpSession session){
	public String delete(int boardNo, int memberNo, @SessionAttribute("memberNo") int loginMemberNo){
		//UPDATE
		//30일 이내 복구 가능(스케쥴러)
		int result = freeService.delete(boardNo, memberNo, loginMemberNo);
		
		return result == 1 ? "redirect:/free/list.do" : "common/error";
	}
	
	@PostMapping("/editForm.do")
	public String editForm(FreeDto free, Model model, HttpSession session) {
		BoardDto result = freeService.getDetail(free,"edit");

		// 1. resources 이후의 문자열 가져오기
		if (result.getUploadName() != null && result.getUploadPath() != null) {
			int pathIndex = result.getUploadPath().lastIndexOf("resources");

			String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");

			result.setUploadPath(path);
		}

		if (!Objects.isNull(result)) {
			model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
			model.addAttribute("result", result);
			return "board/free/freeEdit";
		} else {
			return "/common/error";
		}
		
	}
	
	
	@PostMapping("/edit.do")
	public String edit(BoardDto board, MultipartFile upload, @SessionAttribute("memberNo") int loginMemberNo) {
		//1. 사용자 검증
		//2. 유효성 검사 -> 제목 : 최대 300byte 
		//3. 제목 / 내용 update
		//4. 업로드한 파일이 있을때 : 기존 파일 삭제 후 새로운 파일로 대체 & upload 테이블 update 
		//5. 업로드한 파일이 없을때 : 할일X
		
		FreeService test = new FreeServiceImpl(null,null,null,null);
		
		logger.info( "게시글 수정 요청: boardNo={}, loginMemberNo={}", board.getBoardNo(),
			       loginMemberNo);
		
		
		int result = freeService.edit(board, upload, loginMemberNo);
		if(result == 1) {
			logger.debug("[뉴스게시판] 게시글 등록 성공: boardNo={}", board.getBoardNo());
			return "redirect:/free/detail.do?boardNo="+board.getBoardNo();
		}else {
			logger.debug("[자유게시판] 게시글 수정 실패 : boardNo={}", board.getBoardNo());
			return "/common/error";
		}
	}

}
