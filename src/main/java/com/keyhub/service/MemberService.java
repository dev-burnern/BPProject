package com.keyhub.service;

import com.keyhub.dao.MemberDAO;
import com.keyhub.dto.MemberDTO;

public class MemberService {
    
    private MemberDAO memberDAO = new MemberDAO();

    // 회원가입 로직
    public boolean register(MemberDTO member) {
        int result = memberDAO.insertMember(member);
        return result > 0;
    }

    // 로그인 로직
    public MemberDTO login(String id, String inputPassword) {
        // 1. 아이디로 회원 정보 가져오기
        MemberDTO member = memberDAO.getMemberById(id);
        
        // 2. 회원이 있고, 비밀번호가 일치하는지 확인 (단순 문자열 비교)
        if (member != null && member.getPassword().equals(inputPassword)) {
            return member; // 로그인 성공 시 회원 정보 반환
        }
        
        return null; // 실패 시 null
    }
 // 정보 수정
    public boolean modifyMember(MemberDTO member) {
        return memberDAO.updateMember(member) > 0;
    }
    
}