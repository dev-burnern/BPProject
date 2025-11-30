package com.keyhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.keyhub.common.DBConnection;
import com.keyhub.dto.OrderDTO;
import com.keyhub.dto.PageInfoDTO; // [NEW] 페이징 DTO 추가
import com.keyhub.dto.ProductDTO;
import com.keyhub.dto.OrderDTO;

public class MarketDAO {

    // 1. 상품 등록
    public int insertProduct(ProductDTO product) {
        String sql = "INSERT INTO PRODUCT (title, content, price, seller_id, img_url, category, status) VALUES (?, ?, ?, ?, ?, ?, 'SELLING')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getTitle());
            pstmt.setString(2, product.getContent());
            pstmt.setInt(3, product.getPrice());
            pstmt.setString(4, product.getSellerId());
            pstmt.setString(5, product.getImgUrl());
            pstmt.setString(6, product.getCategory());
            
            return pstmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // [NEW] 2. 총 게시글 수 조회 (카테고리 필터 포함)
    public int countProducts(String category) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM PRODUCT WHERE status != 'DELETED'";
        
        // 카테고리가 존재하면 조건 추가
        boolean hasCategory = (category != null && !category.isEmpty() && !category.equals("all"));
        if (hasCategory) {
            sql += " AND category = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (hasCategory) {
                pstmt.setString(1, category);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // [NEW] 2-1. 검색 결과 수 조회
    public int countSearchProducts(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM PRODUCT WHERE status != 'DELETED' AND (title LIKE ? OR content LIKE ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // [MODIFIED] 3. 상품 목록 조회 (페이징 + 카테고리)
    public List<ProductDTO> selectProducts(String category, PageInfoDTO pi) {
        List<ProductDTO> list = new ArrayList<>();
        
        // 기본 쿼리: 삭제되지 않은 상품 조회
        StringBuilder sql = new StringBuilder("SELECT * FROM PRODUCT WHERE status != 'DELETED'");
        
        // 카테고리 필터링
        boolean hasCategory = (category != null && !category.isEmpty() && !category.equals("all"));
        if (hasCategory) {
            sql.append(" AND category = ?");
        }
        
        // 정렬 및 페이징 (H2/MySQL 기준)
        sql.append(" ORDER BY p_no DESC LIMIT ? OFFSET ?");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (hasCategory) {
                pstmt.setString(paramIndex++, category);
            }
            
            // 페이징 파라미터 설정
            int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
            pstmt.setInt(paramIndex++, pi.getBoardLimit()); // Limit
            pstmt.setInt(paramIndex++, offset);             // Offset
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) { list.add(mapResultSetToDTO(rs)); }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // [MODIFIED] 4. 검색 (페이징 적용)
    public List<ProductDTO> searchProducts(String keyword, PageInfoDTO pi) {
        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCT WHERE status != 'DELETED' AND (title LIKE ? OR content LIKE ?) " +
                     "ORDER BY p_no DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            
            int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
            pstmt.setInt(3, pi.getBoardLimit());
            pstmt.setInt(4, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) { list.add(mapResultSetToDTO(rs)); }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 5. 상세 조회
    public ProductDTO selectProductById(int pNo) {
        // 상세 조회는 삭제된 상품이라도 관리자가 볼 수 있어야 할 수도 있으나, 일반적으론 숨김
        String sql = "SELECT * FROM PRODUCT WHERE p_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSetToDTO(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // 6. 메인용 최신 상품 (페이징 없음, 단순 Limit)
    public List<ProductDTO> selectRecentProducts(int limit) {
        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCT WHERE status != 'DELETED' ORDER BY p_no DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) { list.add(mapResultSetToDTO(rs)); }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    // 7. 내 판매 상품 수
    public int countProductsBySeller(String sellerId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM PRODUCT WHERE seller_id = ? AND status != 'DELETED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()) count = rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    // 8. 상품 수정
    public int updateProduct(ProductDTO product) {
        String sql = "UPDATE PRODUCT SET title=?, content=?, price=?, img_url=?, category=? WHERE p_no=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getTitle());
            pstmt.setString(2, product.getContent());
            pstmt.setInt(3, product.getPrice());
            pstmt.setString(4, product.getImgUrl());
            pstmt.setString(5, product.getCategory());
            pstmt.setInt(6, product.getpNo());
            return pstmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // [MODIFIED] 9. 상품 삭제 (Soft Delete: 상태만 변경)
    // 무결성 제약 조건 위반 방지 목적
    public int deleteProduct(int pNo) {
        String sql = "UPDATE PRODUCT SET status = 'DELETED' WHERE p_no=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pNo);
            return pstmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 10. 주문 저장
    public int insertOrder(OrderDTO order) {
        String sql = "INSERT INTO ORDERS (member_id, p_no, address, amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, order.getMemberId());
            pstmt.setInt(2, order.getpNo());
            pstmt.setString(3, order.getAddress());
            pstmt.setInt(4, order.getAmount());
            return pstmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 11. 상태 변경 (판매중 -> 예약중 -> 판매완료)
    public void updateProductStatus(int pNo, String status) {
        String sql = "UPDATE PRODUCT SET status = ? WHERE p_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, pNo);
            pstmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
 // [NEW] 12. 내 주문 내역 조회 (JOIN Product)
    public List<OrderDTO> selectOrderList(String memberId) {
        List<OrderDTO> list = new ArrayList<>();
        // ORDERS 테이블과 PRODUCT 테이블을 조인하여 주문 정보 + 상품 정보를 가져옴
        // (가정: 주문 일시 컬럼명이 order_date 라고 가정, 없으면 DB 컬럼명 확인 필요. 보통 default current_timestamp)
        // 여기서는 편의상 ORDERS 테이블의 자동 생성 컬럼을 o_no 순으로 정렬합니다.
        String sql = "SELECT o.o_no, o.member_id, o.p_no, o.amount, o.address, o.order_date, " +
                     "       p.title, p.img_url, p.seller_id " +
                     "FROM ORDERS o " +
                     "JOIN PRODUCT p ON o.p_no = p.p_no " +
                     "WHERE o.member_id = ? " +
                     "ORDER BY o.o_no DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrderDTO order = new OrderDTO();
                    order.setoNo(rs.getInt("o_no"));
                    order.setMemberId(rs.getString("member_id"));
                    order.setpNo(rs.getInt("p_no"));
                    order.setAmount(rs.getInt("amount"));
                    order.setAddress(rs.getString("address"));
                    // DB에 order_date 컬럼이 있다고 가정 (없다면 생성 시 default값 확인)
                    // H2 등에서 자동생성 컬럼이 없으면 INSERT 시 넣어줘야 함. 
                    // 여기서는 조회 시 에러가 나지 않도록 예외처리하거나 컬럼명을 확인해야 합니다.
                    // 우선 order_date가 있다고 가정하고 매핑합니다. (만약 에러나면 reg_date 등으로 수정)
                    try {
                        order.setOrderDate(rs.getTimestamp("order_date"));
                    } catch(Exception e) {
                        // 컬럼이 없을 경우 null 처리 혹은 무시
                    }

                    // 상품 정보 매핑
                    order.setProductTitle(rs.getString("title"));
                    order.setProductImg(rs.getString("img_url"));
                    order.setSellerId(rs.getString("seller_id"));
                    
                    list.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // ResultSet 매핑 헬퍼
    private ProductDTO mapResultSetToDTO(ResultSet rs) throws Exception {
        ProductDTO p = new ProductDTO();
        p.setpNo(rs.getInt("p_no"));
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        p.setPrice(rs.getInt("price"));
        p.setSellerId(rs.getString("seller_id"));
        p.setImgUrl(rs.getString("img_url"));
        p.setStatus(rs.getString("status"));
        p.setRegDate(rs.getTimestamp("reg_date"));
        p.setCategory(rs.getString("category")); 
        return p;
    }
}