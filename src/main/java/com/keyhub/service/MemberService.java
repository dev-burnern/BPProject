package com.keyhub.service;

import com.keyhub.dao.MemberDAO;
import com.keyhub.dto.MemberDTO;

public class MemberService {
    
    private MemberDAO memberDAO = new MemberDAO();

    // 회원가입 로직 (수정됨)
    public int register(MemberDTO member) {
        // 1. 아이디 중복 체크
        // 이미 해당 아이디를 가진 회원이 존재하면 -1 반환
        if (memberDAO.getMemberById(member.getId()) != null) {
            return -1; 
        }

        // 2. 가입 진행
        return memberDAO.insertMember(member);
    }

    // 로그인 로직
    public MemberDTO login(String id, String inputPassword) {
        MemberDTO member = memberDAO.getMemberById(id);
        if (member != null && member.getPassword().equals(inputPassword)) {
            return member;
        }
        return null;
    }

    // 정보 수정
    public boolean modifyMember(MemberDTO member) {
        return memberDAO.updateMember(member) > 0;
    }
}