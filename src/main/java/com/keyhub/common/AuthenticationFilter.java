package com.keyhub.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 모든 요청을 감시합니다.
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    // 🏳️ 백지수표 (로그인 없이 통과 가능한 경로들)
    private static final List<String> WHITE_LIST = Arrays.asList(
        "/",                // 루트
        "/home",            // 메인
        "/member/login",    // 로그인
        "/member/register", // 회원가입
        "/market/list",     // 상품 목록
        "/market/detail",   // 상품 상세
        "/board/list",      // 게시판 목록
        "/board/detail",    // 게시판 상세
        "/image"            // 이미지 로더
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length()); // 프로젝트명 뺀 나머지 경로 (예: /market/list)

        // 1. 정적 리소스(CSS, JS, 이미지 등)는 무조건 통과
        if (path.startsWith("/resources/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. 화이트리스트에 있는 경로인지 확인
        boolean isAllowed = WHITE_LIST.contains(path);

        if (isAllowed) {
            // 통과W
            chain.doFilter(request, response);
        } else {
            // 3. 검문 검색: 세션 확인
            HttpSession session = req.getSession(false); // 세션 없으면 null 반환
            boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

            if (isLoggedIn) {
                chain.doFilter(request, response);
            } else {
                System.out.println("[Filter] 비로그인 접근 차단: " + path);
                res.sendRedirect(contextPath + "/member/login");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}