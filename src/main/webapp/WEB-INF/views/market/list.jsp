<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>중고장터 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
    
    <style>
        .market-header {
            display: flex; justify-content: space-between; align-items: center;
            margin-bottom: 30px;
        }
        .empty-list {
            text-align: center; padding: 100px 0;
            color: var(--text-sub); font-size: 16px;
        }
        .search-result-info {
            margin-bottom: 20px; font-size: 18px;
        }
        
        /* 카테고리 탭 스타일 */
        .category-nav { display: flex; gap: 10px; margin-bottom: 30px; border-bottom: 1px solid #eee; padding-bottom: 10px; }
        .cat-link {
            padding: 8px 16px; border-radius: 20px; font-size: 14px; font-weight: 500;
            color: #666; background: #f8f9fa; transition: all 0.2s; text-decoration: none;
        }
        .cat-link:hover { background: #e9ecef; color: #333; }
        .cat-link.active { background: var(--primary-color, #6366f1); color: white; font-weight: 700; }

        /* [NEW] 페이징 스타일 */
        .pagination-area {
            display: flex; justify-content: center; gap: 8px; margin-top: 50px; margin-bottom: 50px;
        }
        .page-btn {
            display: flex; align-items: center; justify-content: center;
            min-width: 36px; height: 36px; padding: 0 10px;
            border: 1px solid #e5e7eb; border-radius: 6px;
            background: white; color: #374151; font-size: 14px; font-weight: 500;
            transition: all 0.2s; text-decoration: none;
        }
        .page-btn:hover { background: #f9fafb; border-color: #d1d5db; color: #111; }
        .page-btn.active {
            background: var(--primary-color, #6366f1); color: white; border-color: var(--primary-color, #6366f1); pointer-events: none;
        }
        .page-btn.disabled {
            background: #f3f4f6; color: #9ca3af; border-color: #e5e7eb; pointer-events: none; cursor: not-allowed;
        }
    </style>
</head>
<body>
    
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        
        <div class="market-header">
            <div class="section-header" style="border:none; margin:0;">
                <h3>⌨️ 전체 매물 보기</h3>
            </div>
            
            <c:if test="${not empty sessionScope.user}">
                <a href="${ctx}/market/write" class="btn-primary">판매하기 +</a>
            </c:if>
        </div>

        <div class="category-nav">
            <a href="${ctx}/market/list" class="cat-link ${empty currentCategory or currentCategory == 'all' ? 'active' : ''}">전체</a>
            <a href="${ctx}/market/list?category=keyboard" class="cat-link ${currentCategory == 'keyboard' ? 'active' : ''}">키보드</a>
            <a href="${ctx}/market/list?category=keycap" class="cat-link ${currentCategory == 'keycap' ? 'active' : ''}">키캡</a>
            <a href="${ctx}/market/list?category=switch" class="cat-link ${currentCategory == 'switch' ? 'active' : ''}">스위치</a>
            <a href="${ctx}/market/list?category=etc" class="cat-link ${currentCategory == 'etc' ? 'active' : ''}">기타</a>
        </div>

        <c:if test="${not empty searchKeyword}">
            <div class="search-result-info">
                <%-- [MODIFIED] productList.size() 대신 pi.listCount(전체 개수) 사용 --%>
                🔍 <b>'${searchKeyword}'</b> 검색 결과 <span style="color:#888;">(${pi.listCount}건)</span>
                <a href="${ctx}/market/list" style="font-size: 14px; margin-left: 10px; text-decoration: underline; color: #666;">전체보기</a>
            </div>
        </c:if>

        <div class="product-grid">
            <c:choose>
                <%-- 데이터가 없을 때 --%>
                <c:when test="${empty productList}">
                    <div class="empty-list" style="grid-column: 1 / -1;">
                        <c:choose>
                            <c:when test="${not empty searchKeyword}">
                                '${searchKeyword}'에 대한 검색 결과가 없습니다. 😢
                            </c:when>
                            <c:otherwise>
                                등록된 상품이 없습니다. 첫 번째 판매자가 되어보세요!
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                
                <%-- 데이터가 있을 때 --%>
                <c:otherwise>
                    <c:forEach var="p" items="${productList}">
                        <article class="product-card" onclick="location.href='${ctx}/market/detail?pNo=${p.pNo}'">
                            <div class="img-box">
                                <c:choose>
                                    <c:when test="${p.imgUrl == 'no_image.png'}">
                                        <img src="https://placehold.co/300x300/e9ecef/adb5bd?text=No+Image" alt="상품이미지" style="object-fit: cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${ctx}/image?name=${p.imgUrl}" alt="상품이미지">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="info-box">
                                <c:choose>
                                    <c:when test="${p.status == 'SOLD'}">
                                        <span class="status sold" style="background-color:#aaa;">판매완료</span>
                                    </c:when>
                                    <c:when test="${p.status == 'DELETED'}">
                                        <span class="status sold" style="background-color:#ff6b6b;">삭제됨</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status sale">판매중</span>
                                    </c:otherwise>
                                </c:choose>
                                
                                <h4>${p.title}</h4>
                                <p class="price">
                                    <fmt:formatNumber value="${p.price}" type="currency" />
                                </p>
                                <div class="meta" style="display: flex; justify-content: space-between;">
                                    <span>${p.sellerId}</span>
                                    <span><fmt:formatDate value="${p.regDate}" pattern="yyyy-MM-dd"/></span>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- [NEW] 페이징 바 영역 --%>
        <c:if test="${not empty productList and pi.listCount > 0}">
            <div class="pagination-area">
                
                <%-- URL 파라미터 유지를 위한 변수 설정 --%>
                <c:set var="urlParams" value="&category=${currentCategory}&keyword=${searchKeyword}" />

                <%-- [이전] 버튼 --%>
                <c:choose>
                    <c:when test="${pi.currentPage == 1}">
                        <a href="#" class="page-btn disabled"><i class="fas fa-chevron-left">&lt;</i></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/market/list?cpage=${pi.currentPage - 1}${urlParams}" class="page-btn"><i class="fas fa-chevron-left">&lt;</i></a>
                    </c:otherwise>
                </c:choose>

                <%-- [페이지 번호] 버튼 --%>
                <c:forEach var="p" begin="${pi.startPage}" end="${pi.endPage}">
                    <c:choose>
                        <c:when test="${p == pi.currentPage}">
                            <span class="page-btn active">${p}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${ctx}/market/list?cpage=${p}${urlParams}" class="page-btn">${p}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <%-- [다음] 버튼 --%>
                <c:choose>
                    <c:when test="${pi.currentPage == pi.maxPage}">
                        <a href="#" class="page-btn disabled"><i class="fas fa-chevron-right">&gt;</i></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/market/list?cpage=${pi.currentPage + 1}${urlParams}" class="page-btn"><i class="fas fa-chevron-right">&gt;</i></a>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>