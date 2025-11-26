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
        
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

        // 1. 목록 화면 (GET) + 페이징
        if ("/list".equals(pathInfo)) {
            String pageStr = request.getParameter("page");
            int page = 1;
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }

            int pageSize = 10; 
            int blockLimit = 5; 

            int totalCount = boardService.getTotalCount();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            List<BoardDTO> list = boardService.getBoardListPaging(page, pageSize);

            int startPage = (((int)(Math.ceil((double)page / blockLimit))) - 1) * blockLimit + 1;
            int endPage = startPage + blockLimit - 1;
            
            if (endPage > totalPages) {
                endPage = totalPages;
            }

            request.setAttribute("boardList", list);
            request.setAttribute("page", page);             
            request.setAttribute("totalPages", totalPages); 
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
            request.setCharacterEncoding("UTF-8");
            
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
            
            if (user == null || !user.getId().equals(board.getWriterId())) {
                response.sendRedirect(contextPath + "/board/detail?bNo=" + bNo);
                return;
            }
            
            request.setAttribute("board", board);
            request.getRequestDispatcher("/WEB-INF/views/board/update.jsp").forward(request, response);
        }
        // 6. 글 수정 처리 (POST)
        else if ("/update".equals(pathInfo) && "POST".equals(request.getMethod())) {
            request.setCharacterEncoding("UTF-8");
            
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