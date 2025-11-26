package com.keyhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.keyhub.common.DBConnection;
import com.keyhub.dto.MemberDTO;

public class MemberDAO {

    // 회원가입 (INSERT)
    public int insertMember(MemberDTO member) {
        // 암호화 여부와 상관없이 들어온 문자열 그대로 DB에 넣습니다.
        String sql = "INSERT INTO MEMBER (id, password, name, email, role) VALUES (?, ?, ?, ?, 'USER')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getEmail());
            
            return pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 로그인용 회원 조회 (SELECT)
    public MemberDTO getMemberById(String id) {
        String sql = "SELECT * FROM MEMBER WHERE id = ?";
        MemberDTO member = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    member = new MemberDTO();
                    member.setId(rs.getString("id"));
                    member.setPassword(rs.getString("password"));
                    member.setName(rs.getString("name"));
                    member.setEmail(rs.getString("email"));
                    member.setRole(rs.getString("role"));
                    member.setJoinDate(rs.getTimestamp("join_date"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return member;
    }
 // 3. 회원 정보 수정
    public int updateMember(MemberDTO member) {
        String sql = "UPDATE MEMBER SET password = ?, name = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getPassword());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getId());
            
            return pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
