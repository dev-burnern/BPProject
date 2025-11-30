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
        .board-table { width: 100%; border-collapse: collapse; border-top: 2px solid var(--text-main); }
        .board-table th, .board-table td { padding: 15px 10px; text-align: center; border-bottom: 1px solid #eee; }
        .board-table th { background: #f9f9f9; font-weight: 700; }
        .board-table td.title-cell { text-align: left; padding-left: 20px; }
        .board-table a:hover { text-decoration: underline; color: var(--primary-color); }
        
        /* 페이징바 스타일 */
        .pagination { display: flex; justify-content: center; gap: 5px; margin-top: 30px; }
        .page-link {
            display: inline-flex; align-items: center; justify-content: center;
            min-width: 32px; height: 32px; padding: 0 6px;
            border: 1px solid #ddd; border-radius: 4px;
            color: #555; background: white; font-size: 14px; text-decoration: none;
        }
        .page-link:hover { background: #f5f5f5; color: #333; }
        .page-link.active {
            background: var(--primary-color); color: white; border-color: var(--primary-color); font-weight: 700; pointer-events: none;
        }
        .page-link.disabled {
            background: #f9f9f9; color: #ccc; border-color: #eee; cursor: default; pointer-events: none;
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

        <c:if test="${not empty searchKeyword}">
            <div style="margin-bottom: 20px; font-size: 15px;">
                🔍 <b>'${searchKeyword}'</b> 검색 결과 (${pi.listCount}건)
                <a href="${ctx}/board/list" style="margin-left:10px; font-size:13px; text-decoration:underline; color:#888;">전체보기</a>
            </div>
        </c:if>

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
                        <tr><td colspan="5" style="padding: 50px 0; color:#888;">등록된 게시글이 없습니다.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="b" items="${boardList}">
                            <tr>
                                <td>${b.bNo}</td>
                                <td class="title-cell">
                                    <a href="${ctx}/board/detail?bNo=${b.bNo}">
                                        <c:if test="${not empty searchKeyword}">
                                            <%-- 검색어 하이라이팅 (선택사항) --%>
                                            <c:set var="highlight" value="<span style='background:#fff0b3;'>${searchKeyword}</span>"/>
                                            ${b.title.replace(searchKeyword, highlight)}
                                        </c:if>
                                        <c:if test="${empty searchKeyword}">
                                            ${b.title}
                                        </c:if>
                                    </a>
                                </td>
                                <td>${b.writerId}</td>
                                <td><fmt:formatDate value="${b.regDate}" pattern="yyyy-MM-dd"/></td>
                                <td>${b.views}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <%-- 페이징 바 --%>
        <div class="pagination">
            <%-- 검색어 유지를 위한 쿼리스트링 생성 --%>
            <c:set var="qStr" value="${not empty searchKeyword ? '&keyword='.concat(searchKeyword) : ''}" />

            <%-- [이전] 버튼 --%>
            <c:choose>
                <c:when test="${pi.currentPage == 1}">
                    <a href="#" class="page-link disabled">&lt;</a>
                </c:when>
                <c:otherwise>
                    <a href="${ctx}/board/list?page=${pi.currentPage - 1}${qStr}" class="page-link">&lt;</a>
                </c:otherwise>
            </c:choose>

            <%-- 페이지 번호 --%>
            <c:forEach begin="${pi.startPage}" end="${pi.endPage}" var="p">
                <c:choose>
                    <c:when test="${p == pi.currentPage}">
                        <span class="page-link active">${p}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/board/list?page=${p}${qStr}" class="page-link">${p}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <%-- [다음] 버튼 --%>
            <c:choose>
                <c:when test="${pi.currentPage == pi.maxPage or pi.maxPage == 0}">
                    <a href="#" class="page-link disabled">&gt;</a>
                </c:when>
                <c:otherwise>
                    <a href="${ctx}/board/list?page=${pi.currentPage + 1}${qStr}" class="page-link">&gt;</a>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>