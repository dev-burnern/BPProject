<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KeyHub - 홈</title>
    
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
</head>
<body>

    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="main-content">
        
        <section class="hero-banner">
            <div class="banner-content">
                <h2>나만의 커스텀 키보드를 찾아보세요</h2>
                <p>가장 안전하고 빠른 키보드 전문 중고거래</p>
                <a href="${ctx}/market/list" class="btn-primary">매물 보러가기</a>
            </div>
        </section>

        <section class="container section-latest">
            <div class="section-header">
                <h3>🔥 방금 올라온 상품</h3>
                <a href="${ctx}/market/list" class="more-link">더보기 +</a>
            </div>
            
            <div class="product-grid">
                <c:choose>
                    <c:when test="${empty recentProducts}">
                        <div style="grid-column: 1 / -1; text-align: center; padding: 40px; color: #888;">
                            아직 등록된 상품이 없습니다.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${recentProducts}">
                            <article class="product-card" onclick="location.href='${ctx}/market/detail?pNo=${p.pNo}'">
                                <div class="img-box">
                                    <c:choose>
                                        <c:when test="${p.imgUrl == 'no_image.png'}">
                                            <img src="https://placehold.co/300x300/e9ecef/adb5bd?text=No+Image" alt="상품">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${ctx}/image?name=${p.imgUrl}" alt="상품">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="info-box">
                                    <c:choose>
                                        <c:when test="${p.status == 'SOLD'}">
                                            <span class="status sold" style="background-color:#aaa;">판매완료</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status sale">판매중</span>
                                        </c:otherwise>
                                    </c:choose>
                                    
                                    <h4>${p.title}</h4>
                                    
                                    <p class="price">
                                        <fmt:formatNumber value="${p.price}" />원
                                    </p>
                                    
                                    <p class="meta" style="font-size: 12px; color: #888;">
                                        ${p.sellerId} · <fmt:formatDate value="${p.regDate}" pattern="MM-dd"/>
                                    </p>
                                </div>
                            </article>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="container section-community">
            <div class="section-header">
                <h3>📢 커뮤니티 최신글</h3>
                <a href="${ctx}/board/list" class="more-link">더보기 +</a>
            </div>
            <ul class="board-preview-list">
                <c:forEach var="b" items="${recentBoards}">
                    <li style="padding: 15px 0; border-bottom: 1px solid #eee; display:flex; justify-content:space-between;">
                        <div>
                            <span style="color: var(--primary-color); font-weight: bold; margin-right: 5px;">[New]</span>
                            <a href="${ctx}/board/detail?bNo=${b.bNo}" style="font-size: 15px; color:#333;">${b.title}</a>
                        </div>
                    </li>
                </c:forEach>
                
                <c:if test="${empty recentBoards}">
                    <li style="text-align: center; color: #888; padding: 20px;">게시글이 없습니다.</li>
                </c:if>
            </ul>
        </section>

    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
</html>