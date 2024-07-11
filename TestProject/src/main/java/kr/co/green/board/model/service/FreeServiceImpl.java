package kr.co.green.board.model.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dao.FreeDao;
import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.FreeDto;
import kr.co.green.common.paging.PageInfo;
import kr.co.green.common.transaction.TransactionHandler;
import kr.co.green.common.upload.UploadFile_free;
import kr.co.green.common.validation.DataValidation;

@Service
public class FreeServiceImpl implements FreeService {

	private final FreeDao freeDao;
	private final DataValidation dataValidation;
	private final UploadFile_free uploadFile;
	private final TransactionHandler transactionHandler;
	private FreeDto freeDto;

	@Autowired
	public FreeServiceImpl(FreeDao freeDao, DataValidation dataValidation, UploadFile_free uploadFile, TransactionHandler transactionHandler) {
		this.freeDao = freeDao;
		this.dataValidation = dataValidation;
		this.uploadFile = uploadFile;
		this.transactionHandler = transactionHandler;
		this.freeDto = new FreeDto();
	}

	@Override
	public List<FreeDto> freeList(PageInfo pi, BoardDto free) {
		return freeDao.freeList(pi, free);
	}

	@Override
	public int getListCount(FreeDto free) {
		return freeDao.getListCount(free);
	}

	@Override
	// 게시글 정보조회
	public FreeDto getDetail(FreeDto free, String type) {
		HashMap<String ,Object> getTransaction = transactionHandler.getStatus();
		TransactionStatus status = (TransactionStatus) getTransaction.get("status");
		PlatformTransactionManager transactionManager = (PlatformTransactionManager) getTransaction.get("transactionManager");
		
		
		try {
			int result = 0;


			if (type.equals("detail")) {
				// 조회수 증가
				result = freeDao.addViews(free);
			} else if (type.equals("edit")) {
				result = 1;
			}
			if (result == 1) {
				freeDto = freeDao.getDetail(free);
//				transactionManager.commit(status);
				return freeDto;
			} else {
//				transactionManager.rollback(status);
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public int setEnroll(BoardDto board, MultipartFile upload, HttpSession session) {
		if (dataValidation.lengthCheck(board.getBoardTitle(), 100)) {

			if (upload != null && !upload.isEmpty()) {
				uploadFile.upload(board, upload, session);
			} else {
				board.setUploadPath(null);
				board.setUploadName(null);
				board.setUploadOriginName(null);
			}
			int result = freeDao.setEnroll(board);

			if (result == 1 && board.getUploadPath() != null) {
				return freeDao.setUpload(board);
			} else {
				return result;
			}
		}
		return 0;
	}

	@Override
//	public int delete(String boardNo, int memberNo,HttpSession session) {
	public int delete(int boardNo, int memberNo, int loginMemberNo) {
		// 요청한 사용자가 글 작성자가 맞는지 검증
//		if(memberNo == (int)session.getAttribute("memberNo")) {
		if (memberNo == loginMemberNo) {
			int deleteResult = freeDao.delete(boardNo);

			if (deleteResult == 1 && freeDao.getFileName(boardNo) != null) {
				BoardDto resultDto = freeDao.getFileName(boardNo);
				freeDao.uploadDelete(boardNo);
				return uploadFile.delete(resultDto) ? 1 : 0;
			}

			return deleteResult;
		}
		return 0;

	}

	@Override
	public int edit(BoardDto board, MultipartFile upload, int loginMemberNo) {
		int updateResult = 0;
		// 1. 사용자 검증
		// 2. 유효성 검사 -> 제목 : 최대 300byte
		if (board.getMemberNo() == loginMemberNo && dataValidation.lengthCheck(board.getBoardTitle(), 300)) {
			// 3. 제목 / 내용 update
			updateResult = freeDao.edit(board);
			// 4. 업로드한 파일이 있을때 : 기존 파일 삭제 후 새로운 파일로 대체 & upload 테이블 delete & update
			BoardDto getFileName = freeDao.getFileName(board.getBoardNo());
			if (updateResult == 1 && !upload.isEmpty()) {

				uploadFile.upload(board, upload, null);

				if (getFileName != null && uploadFile.delete(getFileName) && board.getUploadName() != null) {
					return freeDao.setUploadUpdate(board) == 1 ? 1 : 0;
				}
			}
			if (updateResult == 1 && getFileName == null) {
				uploadFile.upload(board, upload, null);
				return freeDao.setUpload(board) == 1 ? 1 : 0;

			}

			// 5. 업로드한 파일이 없을때 : 할일X
		}
		return updateResult;

	}

}
