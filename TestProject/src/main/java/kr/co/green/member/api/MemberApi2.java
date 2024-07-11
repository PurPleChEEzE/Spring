package kr.co.green.member.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import kr.co.green.member.model.dto.MemberDto;


@RestController
@RequestMapping("/api/member2")
public class MemberApi2 {
	private static final Logger logger = LogManager.getLogger(MemberApi2.class);
	private static final String BASE_URL = "http://localhost/member";
	private final RestTemplate restTemplate;
	private final Gson gson;
	
	public MemberApi2(RestTemplate restTemplate, Gson gson) {
		this.restTemplate =restTemplate;
		this.gson = gson;
	}
	
	// 요청 : http://localhost/api/member/아이디
	// 중복검사
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCheckId(@PathVariable("id")String id){
		logger.info("/api/member2/" + id + " api 요청받음");
		String url = BASE_URL + "/check-duplicate/" + id;
		
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		logger.debug("Response Status : " +  response.getStatusCode());
		logger.debug("Response Body : " +  response.getBody());
		String getBody = response.getBody();
		HttpStatus getStatusCode = response.getStatusCode();
		
		
		
		logger.debug("Response Status : " + getStatusCode);
		logger.debug("response Body : " + getBody);
		
		MemberDto resultMember = gson.fromJson(response.getBody(), MemberDto.class);
		logger.debug(resultMember.getMemberNo());
		logger.debug(resultMember.getMemberId());
		
		if(resultMember.getMemberNo()==1 && response.getStatusCode() == HttpStatus.OK) {
			return new ResponseEntity<>("false", HttpStatus.OK);
		}else if(resultMember.getMemberNo()==0 && response.getStatusCode() == HttpStatus.OK) {
			return new ResponseEntity<>("true", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("error", HttpStatus.SERVICE_UNAVAILABLE);
		}
		
	}

}
