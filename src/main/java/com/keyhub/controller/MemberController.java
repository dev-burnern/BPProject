package com.keyhub.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.keyhub.dto.CartDTO;
import com.keyhub.dto.MemberDTO;
import com.keyhub.dto.OrderDTO;
import com.keyhub.dto.ProductDTO;
import com.keyhub.service.BoardService;
import com.keyhub.service.CartService;
import com.keyhub.service.MarketService;
import com.keyhub.service.MemberService;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    // 서비스 객체 준비
    private MemberService memberService = new MemberService();
    private CartService cartService = new CartService();
    private BoardService boardService = new BoardService();
    private MarketService marketService = new MarketService();
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo(); 
        String contextPath = request.getContextPath(); 
        
        // 1. 회원가입 화면 (GET)
        if ("/register".equals(pathInfo) && "GET".equals(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/views/member/register.jsp")
                   .forward(request, response);
        }
     // 2. 회원가입 처리 (POST)
        else if ("/register".equals(pathInfo) && "POST".equals(request.getMethod())) {
            MemberDTO member = new MemberDTO();
            member.setId(request.getParameter("id"));
            member.setPassword(request.getParameter("password")); 
            member.setName(request.getParameter("name"));
            member.setEmail(request.getParameter("email"));

            // int형 결과 받기
            int result = memberService.register(member);

            if (result > 0) {
                // 성공 시 로그인 페이지로
                response.sendRedirect(contextPath + "/member/login");
            } else if (result == -1) {
                // [추가] 중복 아이디인 경우
                response.sendRedirect(contextPath + "/member/register?error=duplicate");
            } else {
                // 기타 실패
                response.sendRedirect(contextPath + "/member/register?error=fail");
            }
        }
        // 3. 로그인 화면 (GET)
        else if ("/login".equals(pathInfo) && "GET".equals(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/views/member/login.jsp")
                   .forward(request, response);
        }
        // 4. 로그인 처리 (POST)
        else if ("/login".equals(pathInfo) && "POST".equals(request.getMethod())) {
            String id = request.getParameter("id");
            String pw = request.getParameter("password");
            
            MemberDTO loginMember = memberService.login(id, pw);
            
            if (loginMember != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", loginMember);
                response.sendRedirect(contextPath + "/home");
            } else {
                response.sendRedirect(contextPath + "/member/login?error=fail");
            }
        }
        // 5. 로그아웃 (GET)
        else if ("/logout".equals(pathInfo)) {
            HttpSession session = request.getSession();
            session.invalidate(); 
            response.sendRedirect(contextPath + "/home");
        }
     // 6. 마이페이지 (GET)
        else if ("/mypage".equals(pathInfo)) {
            HttpSession session = request.getSession();
            MemberDTO user = (MemberDTO) session.getAttribute("user");
            
            if (user == null) {
                response.sendRedirect(contextPath + "/member/login");
                return;
            }
            
            // 1. 장바구니 목록
            List<CartDTO> myCartList = cartService.getMyCartList(user.getId());
            request.setAttribute("myCartList", myCartList);
            
            // 2. 작성 글 개수
            int boardCount = boardService.getMyBoardCount(user.getId());
            request.setAttribute("boardCount", boardCount);
            
            // 3. 판매 내역(상품) 개수 가져오기
            List<ProductDTO> myProductList = marketService.getMyProductList(user.getId());
            request.setAttribute("myProductList", myProductList);
            
            // 4. 주문(구매) 내역 가져오기
            List<OrderDTO> myOrderList = marketService.getMyOrderList(user.getId());
            request.setAttribute("myOrderList", myOrderList);
            
            request.getRequestDispatcher("/WEB-INF/views/member/mypage.jsp")
                   .forward(request, response);
        }
     // 7. 회원 정보 수정 화면 (GET)
        else if ("/edit".equals(pathInfo) && "GET".equals(request.getMethod())) {
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect(contextPath + "/member/login");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/views/member/edit.jsp")
                   .forward(request, response);
        }
        
        // 8. 회원 정보 수정 처리 (POST)
        else if ("/edit".equals(pathInfo) && "POST".equals(request.getMethod())) {
            request.setCharacterEncoding("UTF-8");
            
            HttpSession session = request.getSession();
            MemberDTO user = (MemberDTO) session.getAttribute("user");
            
            if (user == null) {
                response.sendRedirect(contextPath + "/member/login");
                return;
            }

            // 폼에서 넘어온 수정 데이터 받기
            String pw = request.getParameter("password");
            String name = request.getParameter("name");
            String email = request.getParameter("email");

            // DTO에 담기 (ID는 세션에서 가져온 그대로 유지)
            MemberDTO updateUser = new MemberDTO();
            updateUser.setId(user.getId());
            updateUser.setPassword(pw);
            updateUser.setName(name);
            updateUser.setEmail(email);
            
            boolean isSuccess = memberService.modifyMember(updateUser);
            
            if (isSuccess) {
                user.setPassword(pw);
                user.setName(name);
                user.setEmail(email);
                session.setAttribute("user", user); // 세션 덮어쓰기
                
                // 수정 후 마이페이지로 이동
                response.sendRedirect(contextPath + "/member/mypage");
            } else {
                response.sendRedirect(contextPath + "/member/edit?error=fail");
            }
        }
    }
}