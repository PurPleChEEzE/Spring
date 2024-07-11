package kr.co.green.board.model.dao;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.paging.PageInfo;

@Repository
public class NewsDao{
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	public NewsDao(SqlSessionTemplate sqlSession) {
		this.sqlSession= sqlSession;
	}
	
	public List<NewsDto> newsList(PageInfo pi, BoardDto news){
		RowBounds rb = new RowBounds(pi.getOffset(), pi.getBoardLimit());
		return sqlSession.selectList("newsMapper.newsList", news, rb);
	}
	
	public int getListCount(NewsDto news) {
		return sqlSession.selectOne("newsMapper.getListCount", news);
	}
	
	public NewsDto getDetail(NewsDto news) {
		return sqlSession.selectOne("newsMapper.getDetail", news);
	}
	
	public int addViews(NewsDto newsDto) {
		return sqlSession.update("newsMapper.addViews", newsDto);
	}

	public int setEnroll(BoardDto board) {
		return sqlSession.insert("newsMapper.setEnroll", board);
	}

	public int setUpload(BoardDto board) {
		if (board.getUploadPath() != null && board.getUploadName() != null && board.getUploadOriginName() != null) {
			return sqlSession.insert("newsMapper.setUpload", board);
		} else {
			return 1; 
		}
		
	}

	public int delete(int boardNo) {
		return sqlSession.update("newsMapper.delete", boardNo);
	}
	
	public int uploadDelete(int boardNo) {
		return sqlSession.delete("newsMapper.uploadDelete", boardNo);
	}

	public BoardDto getFileName(int boardNo) {
		return sqlSession.selectOne("newsMapper.getFileName", boardNo);
	}

	public int edit(BoardDto board) {
		return sqlSession.update("newsMapper.edit", board);
	}
	public int setUploadUpdate(BoardDto board) {
		return sqlSession.update("newsMapper.setUploadUpdate", board);
	}
	
}