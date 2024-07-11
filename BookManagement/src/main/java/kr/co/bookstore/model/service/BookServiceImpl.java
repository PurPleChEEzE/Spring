package kr.co.bookstore.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.bookstore.model.dao.BookDao;
import kr.co.bookstore.model.dto.BookDto;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<BookDto> getList() {
        return bookDao.getList();
    }

    @Override
    public List<BookDto> getDetailList(String bookTitle) {
        return bookDao.getDetailList(bookTitle);
    }

    @Override
    public int updateBookStatus(String bookTitle) {
        return bookDao.updateBookStatus(bookTitle);
    }

    @Override
    public int insertBorrowRecord(BookDto bookDto) {
        return bookDao.insertBorrowRecord(bookDto);
    }

    @Override
    public int returnBook(String bookTitle) {
        return bookDao.returnBook(bookTitle);
    }
    
    @Override
    public List<BookDto> getKpiList(){
    	return bookDao.getKpiList();
    }
    
    @Override
    public List<BookDto> getKpiDetail(String mainKpi){
    	return bookDao.getKpiDetail(mainKpi);
    }
}
