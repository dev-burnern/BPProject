package com.keyhub.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.keyhub.dto.BoardDTO;
import com.keyhub.dto.MemberDTO;
import com.keyhub.dto.PageInfoDTO;
import com.keyhub.dto.ReplyDTO;
import com.keyhub.service.BoardService;
import com.keyhub.service.ReplyService;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // 서비스 객체들 준비
    private BoardService boardService = new BoardService();
    private ReplyService replyService = new ReplyService(); 

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); // [Safety] 한글 처리를 위해 인코딩 설정 명시
        
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

     // 1. 목록 화면 (GET)
        if ("/list".equals(pathInfo)) {
            // 1. 페이지 번호 파싱
            int currentPage = 1;
            if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
                currentPage = Integer.parseInt(request.getParameter("page"));
            }

            // 2. 검색어 파싱
            String keyword = request.getParameter("keyword");

            int pageLimit = 5; 
            int boardLimit = 10; 
            int listCount = 0;
            List<BoardDTO> list = null;

            // 3. 검색어 유무에 따른 분기 처리
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 검색 로직
                listCount = boardService.getSearchCount(keyword);
                list = boardService.getSearchList(keyword, currentPage, boardLimit);
                request.setAttribute("searchKeyword", keyword); // 검색어 유지용
            } else {
                // 일반 목록 로직
                listCount = boardService.getTotalCount();
                list = boardService.getBoardListPaging(currentPage, boardLimit);
            }

            // 4. 페이징 계산 (공통)
            int maxPage = (int) Math.ceil((double) listCount / boardLimit);
            int startPage = (currentPage - 1) / pageLimit * pageLimit + 1;
            int endPage = startPage + pageLimit - 1;
            
            if (endPage > maxPage) { endPage = maxPage; }

            PageInfoDTO pi = new PageInfoDTO(listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage);

            // 5) 결과 전달
            request.setAttribute("boardList", list);
            request.setAttribute("pi", pi);
            
            request.setAttribute("page", currentPage);             
            request.setAttribute("totalPages", maxPage); 
            request.setAttribute("startPage", startPage);   
            request.setAttribute("endPage", endPage);       

            request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
        }
        // 2. 글쓰기 화면 (GET)
        else if ("/write".equals(pathInfo) && "GET".equals(request.getMethod())) {
            if (!isLogin(request)) {
                response.sendRedirect(contextPath + "/member/login");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/board/write.jsp").forward(request, response);
        }
        // 3. 글쓰기 처리 (POST)
        else if ("/write".equals(pathInfo) && "POST".equals(request.getMethod())) {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            
            HttpSession session = request.getSession();
            MemberDTO user = (MemberDTO) session.getAttribute("user");
            
            BoardDTO board = new BoardDTO();
            board.setTitle(title);
            board.setContent(content);
            board.setWriterId(user.getId());
            
            boardService.writeBoard(board);
            
            response.sendRedirect(contextPath + "/board/list");
        }
        // 4. 상세 보기 (GET)
        else if ("/detail".equals(pathInfo)) {
            int bNo = Integer.parseInt(request.getParameter("bNo"));
            
            // 게시글 내용 가져오기 (조회수 증가 포함)
            BoardDTO board = boardService.getBoardDetail(bNo);
            
            // 댓글 목록 가져오기 (replyService 사용)
            List<ReplyDTO> replyList = replyService.getReplyList(bNo);
            request.setAttribute("replyList", replyList);
            
            request.setAttribute("board", board);
            request.getRequestDispatcher("/WEB-INF/views/board/detail.jsp").forward(request, response);
        }
        // 5. 글 수정 화면 (GET)
        else if ("/update".equals(pathInfo) && "GET".equals(request.getMethod())) {
            int bNo = Integer.parseInt(request.getParameter("bNo"));
            BoardDTO board = boardService.getBoardDetail(bNo);
            
            HttpSession session = request.getSession();
            MemberDTO user = (MemberDTO) session.getAttribute("user");
            
            // 본인 확인
            if (user == null || !user.getId().equals(board.getWriterId())) {
                response.sendRedirect(contextPath + "/board/detail?bNo=" + bNo);
                return;
            }
            
            request.setAttribute("board", board);
            request.getRequestDispatcher("/WEB-INF/views/board/update.jsp").forward(request, response);
        }
        // 6. 글 수정 처리 (POST)
        else if ("/update".equals(pathInfo) && "POST".equals(request.getMethod())) {
            int bNo = Integer.parseInt(request.getParameter("bNo"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            
            BoardDTO board = new BoardDTO();
            board.setbNo(bNo);
            board.setTitle(title);
            board.setContent(content);
            
            boolean isSuccess = boardService.modifyBoard(board);
            
            if (isSuccess) {
                response.sendRedirect(contextPath + "/board/detail?bNo=" + bNo);
            } else {
                response.sendRedirect(contextPath + "/board/list");
            }
        }
        // 7. 글 삭제 (GET)
        else if ("/delete".equals(pathInfo)) {
            int bNo = Integer.parseInt(request.getParameter("bNo"));
            
            BoardDTO board = boardService.getBoardDetail(bNo);
            HttpSession session = request.getSession();
            MemberDTO user = (MemberDTO) session.getAttribute("user");
            
            // 본인 확인
            if (user == null || !user.getId().equals(board.getWriterId())) {
                response.sendRedirect(contextPath + "/board/detail?bNo=" + bNo);
                return;
            }
            
            boardService.removeBoard(bNo);
            response.sendRedirect(contextPath + "/board/list");
        }
    }

    private boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("user") != null;
    }
}