<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>정보 수정 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    
    <style>
        .auth-container {
            max-width: 400px; margin: 100px auto; padding: 40px;
            background: white; border-radius: var(--radius-md);
            box-shadow: var(--shadow-md); border: 1px solid var(--border-color);
        }
        .auth-title { text-align: center; margin-bottom: 30px; font-size: 24px; font-weight: 700; }
        .form-group { margin-bottom: 20px; }
        .form-label { display: block; margin-bottom: 8px; font-weight: 500; }
        .form-input { width: 100%; padding: 12px; border: 1px solid var(--border-color); border-radius: 4px; }
        /* 읽기 전용 스타일 */
        .form-input[readonly] { background-color: #f5f5f5; color: #888; cursor: not-allowed; }
        .btn-full { width: 100%; margin-top: 10px; text-align: center; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="main-content">
        <div class="auth-container">
            <h2 class="auth-title">내 정보 수정</h2>
            
            <form action="${ctx}/member/edit" method="post">
                <div class="form-group">
                    <label class="form-label">아이디 (수정 불가)</label>
                    <input type="text" name="id" class="form-input" value="${sessionScope.user.id}" readonly>
                </div>
                
                <div class="form-group">
                    <label class="form-label">비밀번호</label>
                    <input type="text" name="password" class="form-input" value="${sessionScope.user.password}" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label">이름</label>
                    <input type="text" name="name" class="form-input" value="${sessionScope.user.name}" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label">이메일</label>
                    <input type="email" name="email" class="form-input" value="${sessionScope.user.email}" required>
                </div>
                
                <button type="submit" class="btn-primary btn-full">수정 완료</button>
                <a href="${ctx}/member/mypage" class="btn-secondary btn-full" style="display:block; box-sizing:border-box; background:#eee; color:#333;">취소</a>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>