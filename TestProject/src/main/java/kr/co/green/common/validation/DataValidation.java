package kr.co.green.common.validation;

import org.springframework.stereotype.Component;

@Component
public class DataValidation {

	public boolean lengthCheck(String data, int dataLength) {
//		int byteLength = 0;
//		
//		
//		//data : "안녕하세요"
//		//['안', '녕', '하', '세', '요']
//		for(char c: data.toCharArray()) {
//			if(Character.toString(c).matches("[a-zA-Z0-9]")) {
//				byteLength +=1;
//			}else if(Character.toString(c).matches("[ㄱ-ㅎㅏ-ㅣ가-힣]")) {
//				byteLength +=3;
//			}
//		}
		//아래의 코드는 대충 위의 byteLength를 구하는 코드 
		int byteLength = data.getBytes().length;
		
		if(byteLength > dataLength) {
			return false;
		}else {
			return true;
		}
	}
}
