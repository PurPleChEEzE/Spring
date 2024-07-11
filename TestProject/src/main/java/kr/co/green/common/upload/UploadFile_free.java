package kr.co.green.common.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.green.board.model.dto.BoardDto;

@Component
public class UploadFile_free {
	public static final String UPLOAD_PATH = "C:\\dev\\workspace\\8. Spring\\TestProject\\src\\main\\webapp\\resources\\uploads_free\\";
	
	public boolean delete(BoardDto board) {
		File file = new File(board.getUploadPath() + board.getUploadName());
		return file.delete();
	}
	
	public void upload(BoardDto board, MultipartFile upload, HttpSession session) {
		if(!upload.isEmpty()) {
			//원본파일이름
			String originName = upload.getOriginalFilename();
			
			//파일 확장자
			// -> 마지막으로 오는 . 앞까지로 짜르겠다는 뜻
			String extension = originName.substring(originName.lastIndexOf('.'));
			
			//현재 년-월-일-시-분-초
			//현재시간 가져와 저장
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");
			
			String output = now.format(formatter);
			
			//랜덤문자열 생성
			int stringLength = 8;
			String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			
			Random random = new Random();
			
			String randomString = random.ints(stringLength, 0, characters.length())	//길이가 stringLength인 난수 생성
					.mapToObj(characters::charAt)	// 위에서 생성한 난수를 characters에 맵핑시켜줌 
					.map(Object::toString)	// char -> string으로 형변환(한글자)
					.collect(Collectors.joining());	// 변환된 문자열들을 하나로 결합시킴
			
			
			//ex) 파일명 : 날짜_랜덤문자열.확장자
			//ex) 240628144013_Kenguso.jpg
			
			String fileName = output + "_" + randomString + extension;
			
			String filePathName = UPLOAD_PATH + fileName;
			
			Path filePath = Paths.get(filePathName);
			
			try {
				upload.transferTo(filePath);
				
				board.setUploadPath(UPLOAD_PATH);
				board.setUploadName(fileName);
				board.setUploadOriginName(originName);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
		}
	}
	
}
