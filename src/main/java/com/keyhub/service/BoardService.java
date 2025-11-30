package com.keyhub.service;

import java.util.List;
import com.keyhub.dao.BoardDAO;
import com.keyhub.dto.BoardDTO;

public class BoardService {
    
    private BoardDAO boardDAO = new BoardDAO();

    // 글 등록
    public boolean writeBoard(BoardDTO board) {
        return boardDAO.insertBoard(board) > 0;
    }

    // 목록 조회
    public List<BoardDTO> getBoardList() {
        return boardDAO.selectAllBoards();
    }

    // 상세 조회 (조회수 증가 포함)
    public BoardDTO getBoardDetail(int bNo) {
        boardDAO.updateViews(bNo); // 조회수 +1
        return boardDAO.selectBoardById(bNo); // 데이터 가져오기
    }
	 // 글 수정
    public boolean modifyBoard(BoardDTO board) {
        return boardDAO.updateBoard(board) > 0;
    }
	 // 글 삭제
    public boolean removeBoard(int bNo) {
        return boardDAO.deleteBoard(bNo) > 0;
    }
    // 메인용 최신 글
    public List<BoardDTO> getRecentBoards(int limit) {
        return boardDAO.selectRecentBoards(limit);
    }
 // 내가 쓴 글 개수 가져오기
    public int getMyBoardCount(String writerId) {
        return boardDAO.countBoardsByWriter(writerId);
    }
 // 전체 글 개수 가져오기
    public int getTotalCount() {
        return boardDAO.countAllBoards();
    }

    // 페이징된 목록 가져오기
    public List<BoardDTO> getBoardListPaging(int page, int pageSize) {
        // page 1 -> offset 0
        // page 2 -> offset 10
        // page 3 -> offset 20
        int offset = (page - 1) * pageSize;
        return boardDAO.selectBoardListByPage(offset, pageSize);
    }
 // [NEW] 검색 게시글 수
    public int getSearchCount(String keyword) {
        return boardDAO.countSearchBoards(keyword);
    }

    // [NEW] 검색 게시글 목록
    public List<BoardDTO> getSearchList(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return boardDAO.selectSearchBoards(keyword, offset, pageSize);
    }
}