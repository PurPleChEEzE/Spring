package kr.co.bookstore.model.service;

import java.util.List;

import kr.co.bookstore.model.dto.BookDto;

public interface BookService {
	List<BookDto> getList();

	List<BookDto> getDetailList(String bookTitle);
	
    int updateBookStatus(String bookTitle);
    
    int insertBorrowRecord(BookDto bookDto);
    
    int returnBook(String bookTitle);
    
    List<BookDto> getKpiList();

	List<BookDto> getKpiDetail(String mainKpi);

    
}
