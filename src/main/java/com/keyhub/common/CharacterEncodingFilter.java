package com.keyhub.common;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {

    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // web.xml에서 설정한 "UTF-8" 값을 가져옵니다.
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 1. 요청 인코딩 설정 
        request.setCharacterEncoding(encoding);
        
        // 2. 응답 인코딩 설정 
        response.setCharacterEncoding(encoding);

        // 3. 다음 흐름(Servlet 등)으로 넘김
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 필터 종료 시 처리할 로직이 필요하면 여기에 작성
    }
}