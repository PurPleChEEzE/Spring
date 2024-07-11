package kr.co.green.board.model.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dao.NewsDao;
import kr.co.green.board.model.dto.BoardDto;
import kr.co.green.board.model.dto.NewsDto;
import kr.co.green.common.paging.PageInfo;
import kr.co.green.common.transaction.TransactionHandler;
import kr.co.green.common.upload.UploadFile_news;
import kr.co.green.common.validation.DataValidation;

@Service
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = LogManager.getLogger(NewsServiceImpl.class);

    private final NewsDao newsDao;
    private final DataValidation dataValidation;
    private final UploadFile_news uploadFile;
    private final TransactionHandler transactionHandler;
    private NewsDto newsDto;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, DataValidation dataValidation, UploadFile_news uploadFile, TransactionHandler transactionHandler) {
        this.newsDao = newsDao;
        this.dataValidation = dataValidation;
        this.uploadFile = uploadFile;
        this.transactionHandler = transactionHandler;
        this.newsDto = new NewsDto();
    }

    @Override
    public List<NewsDto> newsList(PageInfo pi, BoardDto news) {
        return newsDao.newsList(pi, news);
    }

    @Override
    public int getListCount(NewsDto news) {
        return newsDao.getListCount(news);
    }

    @Override
    public NewsDto getDetail(NewsDto news, String type) {
        logger.info("게시글 조회 요청: boardNo = {}", news.getBoardNo());
        try {
            int result = 0;

            if (type.equals("detail")) {
                // 조회수 증가
                result = newsDao.addViews(news);
            } else if (type.equals("edit")) {
                result = 1;
            }
            if (result == 1) {
                newsDto = newsDao.getDetail(news);

                logger.debug("상세 조회 성공: boardNo = {}", news.getBoardNo());
                return newsDto;
            } else {
                logger.debug("상세 조회 실패: boardNo = {}", news.getBoardNo());
                return null;
            }
        } catch (Exception e) {
            logger.debug("게시글 상세 조회 중 예외 발생, Exception : ");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public int setEnroll(BoardDto board, MultipartFile upload, HttpSession session, HttpServletResponse response) {
        logger.info("게시글 등록 요청: BoardDto={}, MultipartFile={}", board, upload);
        if (!board.getBoardTitle().equals("") && dataValidation.lengthCheck(board.getBoardTitle(), 100)
                && !board.getBoardContent().equals("") && dataValidation.lengthCheck(board.getBoardContent(), 1000)) {

            if (upload != null && !upload.isEmpty()) {
                uploadFile.upload(board, upload, session);
            } else {
                board.setUploadPath(null);
                board.setUploadName(null);
                board.setUploadOriginName(null);
            }

            int result = newsDao.setEnroll(board);
            if(board.getUploadName() != null) {
            	logger.debug("파일 업로드 성공: boardNo={}", board.getBoardNo());
            }

            if (result == 1 && board.getUploadPath() != null) {
                logger.debug("게시글 등록 및 파일 업로드 정보 저장 성공 : boardNo={}", board.getBoardNo());
                return newsDao.setUpload(board);
            } else {
                logger.debug("게시글 등록 성공(업로드 파일 없음) : boardNo={}", board.getBoardNo());
                return result;
            }
        }
        logger.debug("게시글 등록 실패: 사용자 검증 실패");

        try {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            out.println("<script> alert('값이 비었거나 조건에 맞지 않습니다.');");
            out.println("history.go(-1); </script>");
            out.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(int boardNo, int memberNo, int loginMemberNo) {
        if (memberNo == loginMemberNo) {
            logger.info("게시글 삭제 요청: boardNo={}, memberNo={}, loginMemberNo={}", boardNo, memberNo, loginMemberNo);

            int deleteResult = newsDao.delete(boardNo);

            if (deleteResult == 1 && newsDao.getFileName(boardNo) != null) {
                BoardDto resultDto = newsDao.getFileName(boardNo);
                newsDao.uploadDelete(boardNo);

                boolean deleteSuccess = uploadFile.delete(resultDto);
                if (deleteSuccess) {
                    logger.debug(" 파일 삭제 성공: boardNo={}, boardUploadName={}", boardNo, resultDto.getUploadName());
                    return 1;
                } else {
                    logger.debug(" 파일 삭제 실패: boardNo={}, boardUploadName={}", boardNo, resultDto.getUploadName());
                    return 0;
                }
            }
            if (deleteResult == 1) {
                logger.debug("게시글 삭제 성공: boardNo={}", boardNo);
                return deleteResult;
            } else {
                logger.debug("게시글 삭제 실패: boardNo={}", boardNo);
                return 0;
            }

        }
        logger.debug("게시글 삭제 실패: 사용자 검증 실패");
        return 0;
    }

    @Override
    public int edit(BoardDto board, MultipartFile upload, int loginMemberNo) {
    	HashMap<String ,Object> getTransaction = transactionHandler.getStatus();
		TransactionStatus status = (TransactionStatus) getTransaction.get("status");
		PlatformTransactionManager transactionManager = (PlatformTransactionManager) getTransaction.get("transactionManager");
    	
        int updateResult = 0;
        logger.info("게시글 수정 요청: BoardDto={}, loginMemberNo={}", board, loginMemberNo);
        // 1. 사용자 검증
        // 2. 유효성 검사 -> 제목 : 최대 300byte
        if (board.getMemberNo() == loginMemberNo && dataValidation.lengthCheck(board.getBoardTitle(), 300)) {
            // 3. 제목 / 내용 update

            updateResult = newsDao.edit(board);
            // 4. 업로드한 파일이 있을때 : 기존 파일 삭제 후 새로운 파일로 대체 & upload 테이블 delete & update
            BoardDto getFileName = newsDao.getFileName(board.getBoardNo());
            if (updateResult == 1 && !upload.isEmpty()) {
                uploadFile.upload(board, upload, null);
                
                if(board.getUploadPath() == null && board.getUploadName() == null && board.getUploadOriginName() == null) {
                	transactionManager.rollback(status);
                }
                transactionManager.commit(status);
                logger.debug("기존 파일 삭제 성공: boardNo={}, boardUploadName={}", board.getBoardNo(), getFileName.getUploadName());
                
                
                if (getFileName != null && uploadFile.delete(getFileName) && board.getUploadName() != null) {
                    int setuploadUpdateResult = newsDao.setUploadUpdate(board);
                    if (setuploadUpdateResult == 1) {
                        logger.debug("새로운 파일 업로드 성공: boardNo={}, boardUploadName={}", board.getBoardNo(), board.getUploadName());
                        return 1;
                    } else {
                    	transactionManager.rollback(status);
                        logger.debug("새로운 파일 업로드 실패: boardNo={}, boardUploadName={}", board.getBoardNo(), board.getUploadName());
                        return 0;
                    }
                }

            }
            if (updateResult == 1 && getFileName == null) {
                uploadFile.upload(board, upload, null);

                int updateUploadResult = newsDao.setUpload(board);

                if (updateUploadResult == 1) {
                    logger.debug("새로운 파일 업로드 성공: boardNo={}, boardUploadName={}", board.getBoardNo(), board.getUploadName());
                    return 1;
                } else {
                	transactionManager.rollback(status);
                    logger.debug("새로운 파일 업로드 실패: boardNo={}, boardUploadName={}", board.getBoardNo(), board.getUploadName());
                    return 0;
                }

            }

            // 5. 업로드한 파일이 없을때 : 할일X
            transactionManager.commit(status);
            logger.debug("게시글 수정 성공: boardNo={}", board.getBoardNo());
            return updateResult;
        } else {
        	transactionManager.rollback(status);
            logger.debug("게시글 등록 실패: 제목 길이 검증 실패");
            return updateResult;
        }
    }
}
