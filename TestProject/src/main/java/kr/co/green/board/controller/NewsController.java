package kr.co.green.board.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.board.model.service.NewsServiceImpl;
import kr.co.green.common.paging.PageInfo;
import kr.co.green.common.paging.Pagination;

@Controller
@RequestMapping("/news")
public class NewsController {
	private static final Logger logger = LogManager.getLogger(NewsController.class);
	
	private final NewsServiceImpl newsService;
	private final Pagination pagination;

	@Autowired
	public NewsController(NewsServiceImpl newsService, Pagination pagination) {
		this.newsService = newsService;
		this.pagination = pagination;
	}

	@GetMapping("/list.do")
	public String newsList(Model model, @RequestParam(value = "cpage", defaultValue = "1") int cpage, NewsDto news) {
		// RequestParam annotation : 쿼리스트링을 받을때
		// value : 쿼리스트링 키

		// 1. 전체 게시글 수 구하기(페이징 처리)
		int listCount = newsService.getListCount(news);
		int pageLimit = 5;
		int boardLimit = 8;
		int row = listCount - (cpage - 1) * boardLimit;

		PageInfo pi = pagination.getPageInfo(listCount, cpage, pageLimit, boardLimit);

		List<NewsDto> list = newsService.newsList(pi, news);

		for (NewsDto item : list) {
			if (item.getUploadName() != null && item.getUploadPath() != null) {
				int pathIndex = item.getUploadPath().lastIndexOf("resources");
				String path = "/" + item.getUploadPath().substring(pathIndex).replace("\\", "/");
				item.setUploadPath(path);

			}
		}

		model.addAttribute("row", row);
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);// 객체 바인딩

		return "board/news/newsList";
	}

	@GetMapping("/detail.do")
	public String newsDetail(NewsDto news, Model model, HttpSession session, @RequestParam(value = "boardNo", defaultValue = "null") int boardNo) {
		BoardDto result = newsService.getDetail(news, "detail");
		
		logger.info("상세 조회 요청: boardNo={}, memberNo={}",
			       news.getBoardNo(), session.getAttribute("memberNo"));

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
			
			return "board/news/newsDetail";
		} else {
			logger.debug("상세 조회 실패: boardNo = {}", boardNo);
			return "/common/error";
		}
	}

	@GetMapping("/enrollForm.do")
	public String enrollForm(Model model, HttpSession session) {
	    logger.info("게시글 등록 폼 요청: memberName={}", session.getAttribute("memberName"));
		model.addAttribute("memberName", session.getAttribute("memberName"));
		return "board/news/newsEnroll";
	}

	@RequestMapping("/enroll.do")
	public String setEnroll(BoardDto board, MultipartFile upload, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		board.setMemberNo((int) session.getAttribute("memberNo"));
		
		logger.info("[뉴스게시판] 게시글 등록 요청: memberNo={}",
			       session.getAttribute("memberNo"));

		int result = newsService.setEnroll(board, upload, session, response);

		if (result == 1) {
			logger.debug("[자유게시판] 게시글 등록 성공: boardNo={}", board.getBoardNo());
			redirectAttributes.addFlashAttribute("icon", "success");
			redirectAttributes.addFlashAttribute("text", "게시글이 등록되었습니다. ");
			return "redirect:/news/list.do";
		} else {
			logger.debug("[자유게시판] 게시글 등록 실패: boardNo={}", board.getBoardNo());
			return "/common/error";
		}
	}

	@GetMapping("/delete.do")
	public String delete(int boardNo, int memberNo, @SessionAttribute("memberNo") int loginMemberNo, RedirectAttributes redirectAttributes) {
		logger.info( "게시글 삭제 요청: memberNo={}", loginMemberNo);
		int result = newsService.delete(boardNo, memberNo, loginMemberNo);

		
		if(result == 1) {
			logger.debug("[뉴스게시판] 게시글 삭제 성공: boardNo={}", boardNo);
			redirectAttributes.addFlashAttribute("icon", "success");
			redirectAttributes.addFlashAttribute("text", "게시글이 삭제되었습니다. ");
			return "redirect:/news/list.do";
		}else {
			logger.debug("[뉴스게시판] 게시글 삭제 실패: boardNo={}", boardNo);
			return "common/error";
		}
	
	}

	@RequestMapping("/editForm.do")
	public String editForm(NewsDto news, Model model, HttpSession session) {
		BoardDto result = newsService.getDetail(news, "edit");

		// 1. resources 이후의 문자열 가져오기
		if (result.getUploadName() != null && result.getUploadPath() != null) {
			int pathIndex = result.getUploadPath().lastIndexOf("resources");

			String path = "/" + result.getUploadPath().substring(pathIndex).replace("\\", "/");

			result.setUploadPath(path);
		}

		if (!Objects.isNull(result)) {
			model.addAttribute("loginMemberNo", session.getAttribute("memberNo"));
			model.addAttribute("result", result);
			return "board/news/newsEdit";
		} else {
			return "/common/error";
		}

	}

	@RequestMapping("/edit.do")
	public String edit(BoardDto board, MultipartFile upload, @SessionAttribute("memberNo") int loginMemberNo, RedirectAttributes redirectAttributes) {
		// 1. 사용자 검증
		// 2. 유효성 검사 -> 제목 : 최대 300byte
		// 3. 제목 / 내용 update
		// 4. 업로드한 파일이 있을때 : 기존 파일 삭제 후 새로운 파일로 대체 & upload 테이블 update
		// 5. 업로드한 파일이 없을때 : 할일X
		logger.info( "게시글 수정 요청: boardNo={}, loginMemberNo={}", board.getBoardNo(),
			       loginMemberNo);
		
		int result = newsService.edit(board, upload, loginMemberNo );
		if (result == 1) {
			logger.debug("[뉴스게시판] 게시글 수정 성공: boardNo={}", board.getBoardNo());
			redirectAttributes.addFlashAttribute("icon", "success");
			redirectAttributes.addFlashAttribute("text", "게시글이 수정되었습니다. ");
			return "redirect:/news/detail.do?boardNo=" + board.getBoardNo();
		} else {
			logger.debug("[자유게시판] 게시글 수정 실패 : boardNo={}", board.getBoardNo());
			return "/common/error";
		}
	}

}
