package com.keyhub.dto;

public class PageInfo {
    
    private int listCount;      // 전체 게시글 수
    private int currentPage;    // 현재 페이지 (사용자가 요청한 페이지)
    private int pageLimit;      // 하단에 보여질 페이지 버튼의 최대 개수 (예: 5개)
    private int boardLimit;     // 한 페이지에 보여질 게시글의 최대 개수 (예: 8개)
    
    private int maxPage;        // 가장 마지막 페이지 번호 (총 페이지 수)
    private int startPage;      // 하단 페이지 버튼의 시작 번호
    private int endPage;        // 하단 페이지 버튼의 끝 번호
    
    public PageInfo() {}

    public PageInfo(int listCount, int currentPage, int pageLimit, int boardLimit, int maxPage, int startPage, int endPage) {
        this.listCount = listCount;
        this.currentPage = currentPage;
        this.pageLimit = pageLimit;
        this.boardLimit = boardLimit;
        this.maxPage = maxPage;
        this.startPage = startPage;
        this.endPage = endPage;
    }

    // Getters and Setters
    public int getListCount() { return listCount; }
    public void setListCount(int listCount) { this.listCount = listCount; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageLimit() { return pageLimit; }
    public void setPageLimit(int pageLimit) { this.pageLimit = pageLimit; }
    public int getBoardLimit() { return boardLimit; }
    public void setBoardLimit(int boardLimit) { this.boardLimit = boardLimit; }
    public int getMaxPage() { return maxPage; }
    public void setMaxPage(int maxPage) { this.maxPage = maxPage; }
    public int getStartPage() { return startPage; }
    public void setStartPage(int startPage) { this.startPage = startPage; }
    public int getEndPage() { return endPage; }
    public void setEndPage(int endPage) { this.endPage = endPage; }
}