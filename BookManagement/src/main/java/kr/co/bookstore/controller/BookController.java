package kr.co.bookstore.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import kr.co.bookstore.model.dto.BookDto;
import kr.co.bookstore.model.service.BookServiceImpl;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private static final Logger logger = LogManager.getLogger(BookController.class);
    private final BookServiceImpl bookService;

    @Autowired
    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public ResponseEntity<List<BookDto>> getList() {
        logger.info("/books 요청 받음");
        List<BookDto> bookList = bookService.getList();
        return ResponseEntity.ok(bookList);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<BookDto>> getKpiList() {
    	logger.info("/books/list 요청 받음");
    	List<BookDto> bookKpiList = bookService.getKpiList();
    	return ResponseEntity.ok(bookKpiList);
    }
    @GetMapping("/list/{mainKpi}")
    public ResponseEntity<List<BookDto>> getKpiDetail(@PathVariable String mainKpi) {
    	logger.info("/books/"+ mainKpi + " 요청 받음");
    	List<BookDto> bookKpiDetail = bookService.getKpiDetail(mainKpi);
    	return ResponseEntity.ok(bookKpiDetail);
    }

    @GetMapping("/{bookTitle}")
    public ResponseEntity<List<BookDto>> getDetailList(@PathVariable String bookTitle) {
        logger.info("/books/"+ bookTitle + " 요청 받음");
        List<BookDto> bookDetailList = bookService.getDetailList(bookTitle);
        if (bookDetailList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookDetailList);
    }

    @PostMapping(value = "/{bookTitle}/borrow", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> borrowBook(@PathVariable String bookTitle) {
        logger.info("/books/"+ bookTitle + "/borrow 요청 받음");

        List<BookDto> bookDetailList = bookService.getDetailList(bookTitle);
        if (bookDetailList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BookDto bookDto = bookDetailList.get(0);
        if ("borrowed".equals(bookDto.getBookStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 대여된 책입니다.");
        }

        int updated = bookService.updateBookStatus(bookTitle);
        if (updated == 1) {
            int inserted = bookService.insertBorrowRecord(bookDto);
            if (inserted == 1) {
                return ResponseEntity.ok(bookTitle + " 대여가 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대여 기록을 저장하는 중 오류가 발생했습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("도서 상태를 변경하는 중 오류가 발생했습니다.");
        }
    }

    @PutMapping(value = "/{bookTitle}/return", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> returnBook(@PathVariable String bookTitle) {
        logger.info("/books/" + bookTitle + "/return 요청 받음");

        int updated = bookService.returnBook(bookTitle);
        if (updated == 1) {
            return ResponseEntity.ok(bookTitle + " 반납이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("도서 반납 중 오류가 발생했습니다.");
        }
    }
}
