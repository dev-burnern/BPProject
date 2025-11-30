package com.keyhub.dto;

import java.sql.Timestamp;

public class OrderDTO {
    private int oNo;
    private String memberId;
    private int pNo;
    private String address;
    private int amount;
    private Timestamp orderDate;
    
    // [NEW] 화면 표시용 상품 정보 필드 추가
    private String productTitle;
    private String productImg;
    private String sellerId;

    public int getoNo() { return oNo; }
    public void setoNo(int oNo) { this.oNo = oNo; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public int getpNo() { return pNo; }
    public void setpNo(int pNo) { this.pNo = pNo; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    
    // [NEW] Getters and Setters for Display
    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
    public String getProductImg() { return productImg; }
    public void setProductImg(String productImg) { this.productImg = productImg; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
}