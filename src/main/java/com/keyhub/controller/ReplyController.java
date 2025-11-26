package com.keyhub.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.keyhub.dto.MemberDTO;
import com.keyhub.dto.ReplyDTO;
import com.keyhub.service.ReplyService;

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet {
    
    private ReplyService replyService = new ReplyService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();
        
        // 1. 댓글 등록 (원댓글 & 대댓글)
        if ("/add".equals(pathInfo)) {
            HttpSession session = request.getSession();
            MemberDTO user = (MemberDTO) session.getAttribute("user");
            
            if (user == null) {
                response.sendRedirect(contextPath + "/member/login");
                return;
            }

            int bNo = Integer.parseInt(request.getParameter("bNo"));
            String content = request.getParameter("content");
            // 대댓글일 경우 부모번호가 옴, 없으면 0 (기본값)
            String parentRNoStr = request.getParameter("parentRNo");
            int parentRNo = (parentRNoStr == null || parentRNoStr.isEmpty()) ? 0 : Integer.parseInt(parentRNoStr);

            ReplyDTO reply = new ReplyDTO();
            reply.setbNo(bNo);
            reply.setWriterId(user.getId());
            reply.setContent(content);
            reply.setParentRNo(parentRNo);

            replyService.addReply(reply);
            
            // 다시 게시글 상세로 이동
            response.sendRedirect(contextPath + "/board/detail?bNo=" + bNo);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

        // 2. 댓글 삭제
        if ("/delete".equals(pathInfo)) {
            int rNo = Integer.parseInt(request.getParameter("rNo"));
            int bNo = Integer.parseInt(request.getParameter("bNo")); // 돌아갈 곳을 위해 받음
            
            // (여기서 본인 확인 로직을 추가하면 더 좋음)
            replyService.removeReply(rNo);
            
            response.sendRedirect(contextPath + "/board/detail?bNo=" + bNo);
        }
    }
}