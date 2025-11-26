<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>커뮤니티 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
    
    <style>
        /* 게시판 전용 테이블 스타일 */
        .board-table { width: 100%; border-collapse: collapse; border-top: 2px solid var(--text-main); }
        .board-table th, .board-table td { padding: 15px 10px; text-align: center; border-bottom: 1px solid #eee; }
        .board-table th { background: #f9f9f9; font-weight: 700; }
        .board-table td.title-cell { text-align: left; padding-left: 20px; }
        .board-table a:hover { text-decoration: underline; color: var(--primary-color); }
    	/* 페이징바 스타일 */
        .pagination {
            display: flex; justify-content: center; gap: 5px; margin-top: 30px;
        }
        .page-link {
            display: inline-block; min-width: 32px; height: 32px;
            line-height: 30px; text-align: center;
            border: 1px solid #ddd; border-radius: 4px;
            color: #555; background: white; font-size: 14px;
        }
        .page-link:hover { background: #f5f5f5; }
        .page-link.active {
            background: var(--primary-color); color: white; border-color: var(--primary-color); font-weight: 700;
        }
        .page-link.disabled {
            background: #eee; color: #aaa; cursor: default; pointer-events: none;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="section-header">
            <h3>📢 자유게시판</h3>
            <c:if test="${not empty sessionScope.user}">
                <a href="${ctx}/board/write" class="btn-primary" style="font-size: 14px;">글쓰기</a>
            </c:if>
        </div>

        <table class="board-table">
            <colgroup>
                <col style="width: 8%;">
                <col style="width: auto;">
                <col style="width: 15%;">
                <col style="width: 15%;">
                <col style="width: 10%;">
            </colgroup>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty boardList}">
                        <tr><td colspan="5" style="padding: 50px 0;">등록된 게시글이 없습니다.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="b" items="${boardList}">
                            <tr>
                                <td>${b.bNo}</td>
                                <td class="title-cell">
                                    <a href="${ctx}/board/detail?bNo=${b.bNo}">${b.title}</a>
                                </td>
                                <td>${b.writerId}</td>
                                <td><fmt:formatDate value="${b.regDate}" pattern="yyyy-MM-dd"/></td>
                                <td>${b.views}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table> <div class="pagination">
            
            <c:if test="${startPage > 1}">
                <a href="${ctx}/board/list?page=${startPage - 1}" class="page-link">‹</a>
            </c:if>

            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <c:choose>
                    <c:when test="${i == page}">
                        <span class="page-link active">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/board/list?page=${i}" class="page-link">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${endPage < totalPages}">
                <a href="${ctx}/board/list?page=${endPage + 1}" class="page-link">›</a>
            </c:if>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>