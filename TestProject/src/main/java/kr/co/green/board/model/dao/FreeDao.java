package kr.co.green.board.model.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.paging.PageInfo;

@Repository
public class FreeDao {
	private SqlSessionTemplate sqlSession;

	@Autowired
	public FreeDao(SqlSessionTemplate sqlSession) {
		this.sqlSession= sqlSession;
	}
	
	public List<FreeDto> freeList(PageInfo pi, BoardDto free){
		// 단점 : 성능이 구림 ( 절차가 많아짐) -> 규모가 작을때 주로 사용
		RowBounds rb = new RowBounds(pi.getOffset(), pi.getBoardLimit());
		
		return sqlSession.selectList("freeMapper.freeList", free, rb);
	}

	public int getListCount(FreeDto free) {
		return sqlSession.selectOne("freeMapper.getListCount", free);
	}
	
	public FreeDto getDetail(FreeDto free) {
		return sqlSession.selectOne("freeMapper.getDetail", free);
	}
	
	public int addViews(FreeDto freeDto) {
		return sqlSession.update("freeMapper.addViews", freeDto);
	}
	
	public int setEnroll(BoardDto board) {
		return sqlSession.insert("freeMapper.setEnroll", board);
	}

	public int setUpload(BoardDto board) {
		if (board.getUploadPath() != null && board.getUploadName() != null && board.getUploadOriginName() != null) {
			return sqlSession.insert("freeMapper.setUpload", board);
		} else {
			return 1; 
		}
		
	}

	public int delete(int boardNo) {
		return sqlSession.update("freeMapper.delete", boardNo);
	}
	
	public int uploadDelete(int boardNo) {
		return sqlSession.delete("freeMapper.uploadDelete", boardNo);
	}

	public BoardDto getFileName(int boardNo) {
		return sqlSession.selectOne("freeMapper.getFileName", boardNo);
	}

	public int edit(BoardDto board) {
		return sqlSession.update("freeMapper.edit", board);
	}

	public int setUploadUpdate(BoardDto board) {
		return sqlSession.update("freeMapper.setUploadUpdate", board);
	}



}
