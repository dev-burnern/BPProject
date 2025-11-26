package com.keyhub.service;

import java.util.List;
import com.keyhub.dao.CartDAO;
import com.keyhub.dto.CartDTO;

public class CartService {
    
    private CartDAO cartDAO = new CartDAO();

    // 담기
    public boolean addCart(String memberId, int pNo) {
        return cartDAO.insertCart(memberId, pNo) > 0;
    }

    // 목록
    public List<CartDTO> getMyCartList(String memberId) {
        return cartDAO.selectCartList(memberId);
    }
    
    // 삭제
    public boolean removeCart(int cNo) {
        return cartDAO.deleteCart(cNo) > 0;
    }
}