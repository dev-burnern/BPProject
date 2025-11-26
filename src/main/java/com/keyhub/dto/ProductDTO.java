package com.keyhub.dto;

import java.sql.Timestamp;

public class ProductDTO {
    private int pNo;
    private String title;
    private String content;
    private int price;
    private String sellerId;
    private String imgUrl;
    private String status;
    private Timestamp regDate;
    private String category; 

    
    public int getpNo() { return pNo; }
    public void setpNo(int pNo) { this.pNo = pNo; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getRegDate() { return regDate; }
    public void setRegDate(Timestamp regDate) { this.regDate = regDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}