package com.keyhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.keyhub.common.DBConnection;
import com.keyhub.dto.BoardDTO;

public class BoardDAO {

    // 1. 글 쓰기
    public int insertBoard(BoardDTO board) {
        String sql = "INSERT INTO BOARD (title, content, writer_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setString(3, board.getWriterId());
            
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 글 목록 조회 (최신순)
    public List<BoardDTO> selectAllBoards() {
        List<BoardDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BOARD ORDER BY b_no DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BoardDTO b = new BoardDTO();
                b.setbNo(rs.getInt("b_no"));
                b.setTitle(rs.getString("title"));
                b.setContent(rs.getString("content"));
                b.setWriterId(rs.getString("writer_id"));
                b.setViews(rs.getInt("views"));
                b.setRegDate(rs.getTimestamp("reg_date"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. 상세 조회
    public BoardDTO selectBoardById(int bNo) {
        BoardDTO b = null;
        String sql = "SELECT * FROM BOARD WHERE b_no = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    b = new BoardDTO();
                    b.setbNo(rs.getInt("b_no"));
                    b.setTitle(rs.getString("title"));
                    b.setContent(rs.getString("content"));
                    b.setWriterId(rs.getString("writer_id"));
                    b.setViews(rs.getInt("views"));
                    b.setRegDate(rs.getTimestamp("reg_date"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    // 4. 조회수 증가
    public void updateViews(int bNo) {
        String sql = "UPDATE BOARD SET views = views + 1 WHERE b_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bNo);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 // 5. 글 수정
    public int updateBoard(BoardDTO board) {
        // 제목과 내용만 수정 가능 (작성일, 작성자, 조회수는 유지)
        String sql = "UPDATE BOARD SET title = ?, content = ? WHERE b_no = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setInt(3, board.getbNo());
            
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
 // 6. 글 삭제
    public int deleteBoard(int bNo) {
        String sql = "DELETE FROM BOARD WHERE b_no = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bNo);
            return pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
 // 7. [메인용] 최신 게시글 n개 조회
    public List<BoardDTO> selectRecentBoards(int limit) {
        List<BoardDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BOARD ORDER BY b_no DESC LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BoardDTO b = new BoardDTO();
                    b.setbNo(rs.getInt("b_no"));
                    b.setTitle(rs.getString("title"));
                    // 메인에선 내용까지 필요 없으므로 제목만
                    list.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countBoardsByWriter(String writerId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM BOARD WHERE writer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, writerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1); // 첫 번째 컬럼(COUNT 결과) 가져오기
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
 // 9. [페이징] 전체 글 개수 조회 (총 페이지 수 계산용)
    public int countAllBoards() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM BOARD";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 10. [페이징] 특정 페이지의 글 목록 조회 (LIMIT, OFFSET)
    public List<BoardDTO> selectBoardListByPage(int offset, int limit) {
        List<BoardDTO> list = new ArrayList<>();
        // 글 번호 내림차순(최신순)으로 정렬하고, 앞에서부터 몇 개(OFFSET) 건너뛰고, 몇 개(LIMIT) 가져오기
        String sql = "SELECT * FROM BOARD ORDER BY b_no DESC LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);  // 몇 개 가져올지 (10개)
            pstmt.setInt(2, offset); // 몇 번째부터 시작할지 (0, 10, 20...)
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BoardDTO b = new BoardDTO();
                    b.setbNo(rs.getInt("b_no"));
                    b.setTitle(rs.getString("title"));
                    b.setContent(rs.getString("content"));
                    b.setWriterId(rs.getString("writer_id"));
                    b.setViews(rs.getInt("views"));
                    b.setRegDate(rs.getTimestamp("reg_date"));
                    list.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

