package kr.co.green.member.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.green.member.model.dto.MemberDto;
import kr.co.green.member.model.service.MemberServiceImpl;

@RestController
@RequestMapping("/member")
public class MemberController {
	private static final Logger logger = LogManager.getLogger(MemberController.class);
	private final MemberServiceImpl memberService;
	
	@Autowired
	public MemberController(MemberServiceImpl memberService) {
		this.memberService = memberService;
	}
	
	@GetMapping("/registerForm.do")
	public String getRegisterForm() {
		return "member/register";
	}

	@GetMapping("/check-duplicate/{id}")
	public ResponseEntity<MemberDto> getCheckId(@PathVariable("id") String id)
//	public ResponseEntity<Integer> getCheckId(@PathVariable("id") String id)
	{
		logger.info("/member/check-duplicate/" + id + " 요청받음");
		
		int result = memberService.getCheckId(id);
		
		MemberDto member = new MemberDto();
		member.setMemberNo(result);
		member.setMemberId(id);
		
		if(result==1) {
			return new ResponseEntity<>(member, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(member,HttpStatus.OK);
		}
		
//		if(result==1) {
//			return new ResponseEntity<>(result, HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>(0,HttpStatus.OK);
//		}
	}
//	@PostMapping("/checkId.do")
//	@ResponseBody
//	public String getCheckId(String memberId) {
//		// 아이디 중복 검사
//		int result = memberService.getCheckId(memberId);
////		if(result == 1) { // 사용 불가
////			return "false";
////		} else { // 사용 가능
////			return "true"; 
////		}
//		return intReturn(result, "false", "true");
//	}
	
	@PostMapping("/register.do")
	public String setRegister(MemberDto member) {
		int result = memberService.setRegister(member);
		
		return intReturn(result, "member/login", "common/error");
	}
	
	private String intReturn(int result, String path, String errorPath) {
		if(result == 1) {
			return path;
		} else {
			return errorPath;
		}
	}
	
	@GetMapping("/loginForm.do")
	public String loginForm() {
		return "member/login";
	}
	
	@PostMapping("/login.do")
	public String login(MemberDto member, HttpSession session, RedirectAttributes redirectAttributes) {
		// Spring Security + JWT
		
		// SELECT * FROM member WHERE m_id = #{memberId}
		// 아이디가 일치하는 회원의 정보를 DTO에 담아서 가져오기
		MemberDto loginUser = memberService.login(member);
		
		if(!Objects.isNull(loginUser)) { // 로그인 성공
			session.setAttribute("memberNo", loginUser.getMemberNo());
			session.setAttribute("memberName", loginUser.getMemberName());
			session.setAttribute("memberType", loginUser.getMemberType());
			
			
			
			//addAttribute : int, String 넘길때 사용 
			//					-> 쿼리 파라미터로 전달
			
			//addFlashAttribute : 객체를 넘기고 싶거나, 일회성 변수를 사용하고 싶을 때 사용 
			//						-> 사용 후 사라짐(휘발성)
			redirectAttributes.addFlashAttribute("icon", "success");
			redirectAttributes.addFlashAttribute("title", "로그인 성공");
			redirectAttributes.addFlashAttribute("text", "로그인되었습니다.");
//			redirectAttributes.addAttribute("msg", "로그인되었습니다.");
			return "redirect:/";
		} else {
			redirectAttributes.addFlashAttribute("icon", "error");
			redirectAttributes.addFlashAttribute("title", "로그인 실패");
			redirectAttributes.addFlashAttribute("text", "아이디 또는 비밀번호를 확인해주세요.");
			return "redirect:/member/loginForm.do";
		}
	}
	
	@GetMapping("/logout.do")
	public String logout(HttpServletRequest request) {
		//
		HttpSession session = request.getSession(false);
		if(session !=null) {
			session.invalidate();
		}
		
		return "home";
	}

}










