package kr.co.green.board.model.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.paging.PageInfo;

public interface FreeService {
	//게시글 조회
	List<FreeDto> freeList(PageInfo pi, BoardDto free);
	//전체 게시글 수 조회
	int getListCount(FreeDto free);
	//게시글 조회
	FreeDto getDetail(FreeDto free, String type);
	//게시글 등록
	int setEnroll(BoardDto board, MultipartFile upload, HttpSession session);
//	int delete(String boardNo, int memberNo,HttpSession session);
	//게시글 삭제
	int delete(int boardNo, int memberNo,int loginMemberNo);
	//게시글 수정
	int edit(BoardDto board, MultipartFile upload, int loginMemberNo);
	
}
