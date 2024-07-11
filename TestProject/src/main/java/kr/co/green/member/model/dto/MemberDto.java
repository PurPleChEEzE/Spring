package kr.co.green.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberDto {
	private int memberNo;
	private String memberName;
	private String memberId;
	private String memberPassword;
	private String confirmPassword;
	private String memberIndate;
	private String memberType;
}
