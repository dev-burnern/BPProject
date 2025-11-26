package com.keyhub.dto;

public class PageInfoDTO {
    private int listCount;      // 총 게시글 수
    private int currentPage;    // 현재 페이지
    private int pageLimit;      // 하단 페이지 버튼 수 (예: 5)
    private int boardLimit;     // 한 페이지당 게시글 수 (예: 8)
    private int maxPage;        // 전체 페이지 수
    private int startPage;      // 시작 페이지 번호
    private int endPage;        // 끝 페이지 번호

    public PageInfoDTO(int listCount, int currentPage, int pageLimit, int boardLimit, int maxPage, int startPage, int endPage) {
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
    public int getCurrentPage() { return currentPage; }
    public int getPageLimit() { return pageLimit; }
    public int getBoardLimit() { return boardLimit; }
    public int getMaxPage() { return maxPage; }
    public int getStartPage() { return startPage; }
    public int getEndPage() { return endPage; }
}