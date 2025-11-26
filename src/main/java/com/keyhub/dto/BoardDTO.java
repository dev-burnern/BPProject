package com.keyhub.dto;

import java.sql.Timestamp;

public class BoardDTO {
    private int bNo;            // 글 번호 (PK)
    private String title;       // 제목
    private String content;     // 내용
    private String writerId;    // 작성자 ID
    private int views;          // 조회수
    private Timestamp regDate;  // 작성일

    public int getbNo() { return bNo; }
    public void setbNo(int bNo) { this.bNo = bNo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getWriterId() { return writerId; }
    public void setWriterId(String writerId) { this.writerId = writerId; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public Timestamp getRegDate() { return regDate; }
    public void setRegDate(Timestamp regDate) { this.regDate = regDate; }
}