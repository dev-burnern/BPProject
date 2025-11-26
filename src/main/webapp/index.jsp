<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 
    [진입점]
    사용자가 도메인만 치고 들어왔을 때, 
    즉시 메인 컨트롤러(/home)로 리다이렉트합니다.
--%>
<%
    response.sendRedirect(request.getContextPath() + "/home");
%>