package com.keyhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.keyhub.common.DBConnection;
import com.keyhub.dto.CartDTO;

public class CartDAO {

    // 1. 장바구니 담기
    public int insertCart(String memberId, int pNo) {
        // 이미 담긴 상품인지 체크하는 로직은 생략 (중복 허용)
        String sql = "INSERT INTO CART (member_id, p_no) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberId);
            pstmt.setInt(2, pNo);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 내 장바구니 목록 조회 (상품 정보와 JOIN)
    public List<CartDTO> selectCartList(String memberId) {
        List<CartDTO> list = new ArrayList<>();
        // CART 테이블(c)과 PRODUCT 테이블(p)을 연결
        String sql = "SELECT c.c_no, c.member_id, c.p_no, c.reg_date, " +
                     "       p.title, p.price, p.img_url, p.seller_id " +
                     "FROM CART c " +
                     "JOIN PRODUCT p ON c.p_no = p.p_no " +
                     "WHERE c.member_id = ? " +
                     "ORDER BY c.c_no DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CartDTO c = new CartDTO();
                    c.setCNo(rs.getInt("c_no"));
                    c.setMemberId(rs.getString("member_id"));
                    c.setPNo(rs.getInt("p_no"));
                    c.setRegDate(rs.getTimestamp("reg_date"));
                    
                    // 상품 정보 매핑
                    c.setProductTitle(rs.getString("title"));
                    c.setProductPrice(rs.getInt("price"));
                    c.setProductImg(rs.getString("img_url"));
                    c.setSellerId(rs.getString("seller_id"));
                    
                    list.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. 장바구니 삭제
    public int deleteCart(int cNo) {
        String sql = "DELETE FROM CART WHERE c_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cNo);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}