package kr.co.green.board.model.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.paging.PageInfo;

public interface NewsService {
	//게시글 조회

	
	
	List<NewsDto> newsList(PageInfo pi, BoardDto news);
	int getListCount(NewsDto news);
	NewsDto getDetail(NewsDto news, String type);
	int setEnroll(BoardDto board, MultipartFile upload, HttpSession session, HttpServletResponse response);
	int delete(int boardNo, int memberNo, int loginMemberNo);
	int edit(BoardDto board, MultipartFile upload, int loginMemberNo);
}
