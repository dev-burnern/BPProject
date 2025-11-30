package com.keyhub.service;

import java.util.List;

import com.keyhub.dao.MarketDAO;
import com.keyhub.dto.OrderDTO;
import com.keyhub.dto.PageInfoDTO; // PageInfoDTO import 확인
import com.keyhub.dto.ProductDTO;

public class MarketService {

    private MarketDAO marketDAO = new MarketDAO();

    // 1. 상품 등록
    public int registerProduct(ProductDTO product) {
        return marketDAO.insertProduct(product);
    }

    // [NEW] 2. 카테고리별 전체 게시글 수 조회 (페이징 계산용)
    // Controller의 listCount = marketService.getListCount(category); 부분 해결
    public int getListCount(String category) {
        return marketDAO.countProducts(category);
    }

    // [NEW] 2-1. 검색 게시글 수 조회 (페이징 계산용)
    // 오류가 발생했던 getSearchCount 메서드입니다.
    public int getSearchCount(String keyword) {
        return marketDAO.countSearchProducts(keyword);
    }

    // [MODIFIED] 3. 상품 목록 조회 (페이징 적용)
    // Controller에서 pi(PageInfoDTO)를 넘겨주므로 매개변수 수정 필요
    public List<ProductDTO> getProductList(String category, PageInfoDTO pi) {
        return marketDAO.selectProducts(category, pi);
    }

    // [MODIFIED] 4. 검색 (페이징 적용)
    // 마찬가지로 pi를 매개변수로 받아 DAO로 전달
    public List<ProductDTO> search(String keyword, PageInfoDTO pi) {
        return marketDAO.searchProducts(keyword, pi);
    }

    // 5. 상세 조회
    public ProductDTO getProduct(int pNo) {
        return marketDAO.selectProductById(pNo);
    }

    // 6. 상품 수정
    public int modifyProduct(ProductDTO product) {
        return marketDAO.updateProduct(product);
    }

    // 7. 상품 삭제 (Soft Delete)
    public int removeProduct(int pNo) {
        return marketDAO.deleteProduct(pNo);
    }

    // 8. 구매 처리 (주문 저장 + 상품 상태 변경 트랜잭션)
    public int buyProduct(OrderDTO order) {
        int result = marketDAO.insertOrder(order);
        if(result > 0) {
            // 주문 성공 시 상품 상태를 'SOLD'(판매완료)로 변경
            marketDAO.updateProductStatus(order.getpNo(), "SOLD");
        }
        return result;
    }
 // 9. 메인 화면용 최신 상품 조회 (Limit 개수만큼)
    public List<ProductDTO> getRecentProducts(int limit) {
        return marketDAO.selectRecentProducts(limit);
    }
 // 10. 내 주문 내역 조회
    public List<OrderDTO> getMyOrderList(String memberId) {
        return marketDAO.selectOrderList(memberId);
    }
 // 11. 내 판매 상품 목록 조회
    public List<ProductDTO> getMyProductList(String sellerId) {
        return marketDAO.selectProductsBySeller(sellerId);
    }
}