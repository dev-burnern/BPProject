package com.keyhub.dto;

import java.sql.Timestamp;

public class CartDTO {
    // 1. CART 테이블 컬럼
    private int cNo;
    private String memberId;
    private int pNo;
    private Timestamp regDate;
    
    // 2. JOIN을 통해 가져올 상품 정보 (화면 출력용)
    private String productTitle;
    private int productPrice;
    private String productImg;
    private String sellerId;


    public int getCNo() {
        return cNo;
    }
    public void setCNo(int cNo) {
        this.cNo = cNo;
    }
    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    public int getPNo() {
        return pNo;
    }
    public void setPNo(int pNo) {
        this.pNo = pNo;
    }
    public Timestamp getRegDate() {
        return regDate;
    }
    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }
    public String getProductTitle() {
        return productTitle;
    }
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public int getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
    public String getProductImg() {
        return productImg;
    }
    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}