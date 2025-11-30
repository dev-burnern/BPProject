<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="currentUrl" value="${pageContext.request.requestURI}" />

<header class="site-header">
    <div class="header-container">
        <h1 class="logo">
            <a href="${ctx}/home">KeyHub</a>
        </h1>

        <div class="search-box">
            <c:choose>
                <%-- 게시판 페이지일 때: 게시판 검색 --%>
                <c:when test="${fn:contains(currentUrl, '/board/')}">
                    <form action="${ctx}/board/list" method="get">
                        <input type="text" name="keyword" 
                               placeholder="원하는 게시판의 제목을 검색하세요" 
                               value="${param.keyword}" required>
                        <button type="submit">검색</button>
                    </form>
                </c:when>
                
                <%-- 그 외(메인, 마켓 등): 상품 검색 --%>
                <c:otherwise>
                    <form action="${ctx}/market/list" method="get">
                        <input type="text" name="keyword" 
                               placeholder="원하는 키보드를 검색하세요" 
                               value="${param.keyword}" required>
                        <button type="submit">검색</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <nav class="user-nav">
            <ul>
                <li class="${fn:contains(currentUrl, '/market/') ? 'highlight' : ''}">
                    <a href="${ctx}/market/list">중고장터</a>
                </li>
                <li class="${fn:contains(currentUrl, '/board/') ? 'highlight' : ''}">
                    <a href="${ctx}/board/list">커뮤니티</a>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="${fn:contains(currentUrl, '/member/mypage') ? 'highlight' : ''}">
                            <a href="${ctx}/member/mypage">내 정보</a>
                        </li>
                        <li><a href="${ctx}/member/logout">로그아웃</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${ctx}/member/login">로그인</a></li>
                        <li><a href="${ctx}/member/register">회원가입</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>
</header>