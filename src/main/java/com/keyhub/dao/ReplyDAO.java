package com.keyhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.keyhub.common.DBConnection;
import com.keyhub.dto.ReplyDTO;

public class ReplyDAO {

    // 1. 댓글 등록 (원댓글 & 대댓글 공용)
    public int insertReply(ReplyDTO reply) {
        String sql = "INSERT INTO REPLY (b_no, writer_id, content, parent_r_no) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reply.getbNo());
            pstmt.setString(2, reply.getWriterId());
            pstmt.setString(3, reply.getContent());
            pstmt.setInt(4, reply.getParentRNo()); // 0이면 원댓글
            
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 댓글 목록 조회 (계층형 정렬)
    public List<ReplyDTO> selectAllReplies(int bNo) {
        List<ReplyDTO> list = new ArrayList<>();
        // 정렬 로직:
        // 1차 정렬: 부모 번호가 0이면 자기 번호(r_no)를 쓰고, 아니면 부모 번호(parent_r_no)를 쓴다. -> 그룹핑
        // 2차 정렬: r_no 오름차순 -> 그룹 내에서 순서대로
        String sql = "SELECT * FROM REPLY WHERE b_no = ? " +
                     "ORDER BY CASE WHEN parent_r_no = 0 THEN r_no ELSE parent_r_no END ASC, r_no ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bNo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ReplyDTO r = new ReplyDTO();
                    r.setrNo(rs.getInt("r_no"));
                    r.setbNo(rs.getInt("b_no"));
                    r.setWriterId(rs.getString("writer_id"));
                    r.setContent(rs.getString("content"));
                    r.setParentRNo(rs.getInt("parent_r_no"));
                    r.setRegDate(rs.getTimestamp("reg_date"));
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. 댓글 삭제
    public int deleteReply(int rNo) {
        String sql = "DELETE FROM REPLY WHERE r_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rNo);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}