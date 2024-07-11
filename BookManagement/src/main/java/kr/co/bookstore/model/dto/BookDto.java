package kr.co.bookstore.model.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BookDto {

	private int bookId;
	private String bookTitle;
	private String bookAuthor;
	private String bookStatus;
	private String bookIndate;
	
	private int userId;
	private String bookBorrowed;
	private String bookReturned;
	
	private int subKpi;
	private String kpiName;
	private int mainKpi;
}

