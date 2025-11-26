package com.keyhub.dto;

import java.sql.Timestamp;

public class ReplyDTO {
    private int rNo;
    private int bNo;
    private String writerId;
    private String content;
    private int parentRNo;
    private Timestamp regDate;

    public int getrNo() { return rNo; }
    public void setrNo(int rNo) { this.rNo = rNo; }
    public int getbNo() { return bNo; }
    public void setbNo(int bNo) { this.bNo = bNo; }
    public String getWriterId() { return writerId; }
    public void setWriterId(String writerId) { this.writerId = writerId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getParentRNo() { return parentRNo; }
    public void setParentRNo(int parentRNo) { this.parentRNo = parentRNo; }
    public Timestamp getRegDate() { return regDate; }
    public void setRegDate(Timestamp regDate) { this.regDate = regDate; }
}