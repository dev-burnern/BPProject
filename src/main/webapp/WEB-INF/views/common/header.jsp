<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 컨텍스트 경로를 변수에 담아 편하게 사용 --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<header class="site-header">
    <div class="header-container">
        <h1 class="logo">
            <a href="${ctx}/home">KeyHub</a>
        </h1>

        <div class="search-box">
            <form action="${ctx}/market/list" method="get">
                <input type="text" name="keyword" placeholder="원하는 키보드를 검색하세요" required>
                <button type="submit">검색</button>
            </form>
        </div>

        <nav class="user-nav">
            <ul>
                <li><a href="${ctx}/market/list">중고장터</a></li>
                <li><a href="${ctx}/board/list">커뮤니티</a></li>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="highlight"><a href="${ctx}/member/mypage">내 정보</a></li>
                        <li><a href="${ctx}/member/logout">로그아웃</a></li>
                        <li class="cart-btn"><a href="${ctx}/member/mypage">장바구니</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${ctx}/member/login">로그인</a></li>
                        <li class="highlight"><a href="${ctx}/member/register">회원가입</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>
</header>