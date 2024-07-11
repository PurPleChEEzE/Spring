package kr.co.bookstore.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import kr.co.bookstore.model.dto.BookDto;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookApi {
    private static final Logger logger = LogManager.getLogger(BookApi.class);
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost/books";

    @Autowired
    public BookApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("")
    public ResponseEntity<List<BookDto>> getList() {
        logger.info("/api/books 요청 받음");
        ResponseEntity<BookDto[]> response = restTemplate.getForEntity(BASE_URL, BookDto[].class);
        List<BookDto> bookList = Arrays.asList(response.getBody());
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/{bookTitle}")
    public ResponseEntity<List<BookDto>> getDetailList(@PathVariable String bookTitle) {
        logger.info("/api/books/"+ bookTitle + " 요청 받음");
        ResponseEntity<BookDto[]> response = restTemplate.getForEntity(BASE_URL + "/" + bookTitle, BookDto[].class);
        List<BookDto> bookDetailList = Arrays.asList(response.getBody());
        return ResponseEntity.ok(bookDetailList);
    }

    @PostMapping(value = "/{bookTitle}/borrow", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> borrowBook(@PathVariable String bookTitle) {
        logger.info("/api/books/"+ bookTitle + "/borrow 요청 받음");

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/" + bookTitle + "/borrow", null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("");
        }
    }

    @PutMapping(value = "/{bookTitle}/return", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> returnBook(@PathVariable String bookTitle) {
        logger.info("/api/books/" + bookTitle + "/return 요청 받음");

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/" + bookTitle + "/return",
                org.springframework.http.HttpMethod.PUT,
                null, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("");
        }
    }
}
