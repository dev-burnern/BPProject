<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>글 수정 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    
    <style>
        .write-box { max-width: 800px; margin: 0 auto; }
        .input-title { width: 100%; padding: 15px; font-size: 16px; border: 1px solid var(--border-color); margin-bottom: 15px; }
        .input-content { width: 100%; height: 300px; padding: 15px; border: 1px solid var(--border-color); resize: vertical; font-size: 15px; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="section-header"><h3>글 수정하기</h3></div>
        
        <div class="write-box">
            <form action="${ctx}/board/update" method="post">
                <input type="hidden" name="bNo" value="${board.bNo}">
                
                <input type="text" name="title" class="input-title" value="${board.title}" required>
                <textarea name="content" class="input-content" required>${board.content}</textarea>
                
                <div style="text-align: right; margin-top: 20px;">
                    <a href="${ctx}/board/detail?bNo=${board.bNo}" class="btn-secondary" style="margin-right: 10px;">취소</a>
                    <button type="submit" class="btn-primary">수정 완료</button>
                </div>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>