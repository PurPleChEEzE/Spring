package kr.co.bookstore.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import kr.co.bookstore.model.dto.BookDto;
import java.util.List;

@Repository
public class BookDao {
    private final SqlSessionTemplate sqlSession;

    @Autowired
    public BookDao(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<BookDto> getList() {
        return sqlSession.selectList("bookMapper.getList");
    }

    public List<BookDto> getDetailList(String bookTitle) {
        return sqlSession.selectList("bookMapper.getDetailList", bookTitle);
    }
    

    public int updateBookStatus(String bookTitle) {
        return sqlSession.update("bookMapper.updateBookStatus", bookTitle);
    }

    public int insertBorrowRecord(BookDto bookDto) {
        return sqlSession.insert("bookMapper.insertBorrowRecord", bookDto);
    }

    public int returnBook(String bookTitle) {
        return sqlSession.update("bookMapper.returnBook", bookTitle);
    }
    
    public List<BookDto> getKpiList(){
    	return sqlSession.selectList("poolMapper.getKpiList");
    }
    public List<BookDto> getKpiDetail(String mainKpi){
    	return sqlSession.selectList("poolMapper.getKpiDetail", mainKpi);
    }
}
