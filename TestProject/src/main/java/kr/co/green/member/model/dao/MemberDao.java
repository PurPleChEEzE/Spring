package kr.co.green.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.green.member.model.dto.MemberDto;

@Repository
public class MemberDao {
	private final SqlSessionTemplate sqlSession;
	
	@Autowired
	public MemberDao(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}

	public int getCheckId(String memberId) {
		return sqlSession.selectOne("memberMapper.getCheckId", memberId);
	}

	public int setRegister(MemberDto member) {
		return sqlSession.insert("memberMapper.setRegister", member);
	}

	public MemberDto getInfo(MemberDto member) {
		return sqlSession.selectOne("memberMapper.getInfo", member);
	}
	

	public MemberDto getInfo(String id) {
		return sqlSession.selectOne("memberMapper.getInfoApi", id);
	}

	public int deleteMember(String id) {
		return sqlSession.delete("memberMapper.deleteMember", id);
	}

}











