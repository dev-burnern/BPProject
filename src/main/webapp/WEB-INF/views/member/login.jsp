<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
    
    <style>
        /* 로그인/회원가입 공통 박스 스타일 */
        .auth-container {
            max-width: 400px;
            margin: 100px auto;
            padding: 40px;
            background: white;
            border-radius: var(--radius-md);
            box-shadow: var(--shadow-md);
            border: 1px solid var(--border-color);
        }
        .auth-title { text-align: center; margin-bottom: 30px; font-size: 24px; font-weight: 700; color: var(--primary-color); }
        .form-group { margin-bottom: 20px; }
        .form-label { display: block; margin-bottom: 8px; font-weight: 500; }
        .form-input { 
            width: 100%; padding: 12px; 
            border: 1px solid var(--border-color); border-radius: 4px;
            transition: border-color 0.2s;
        }
        .form-input:focus { border-color: var(--primary-color); }
        .btn-full { width: 100%; text-align: center; margin-top: 10px; }
        
        /* 에러 메시지 스타일 */
        .error-msg {
            background-color: #ffebee; color: #c62828;
            padding: 10px; border-radius: 4px; margin-bottom: 20px;
            font-size: 14px; text-align: center;
        }
        
        .link-group { margin-top: 20px; text-align: center; font-size: 14px; color: var(--text-sub); }
        .link-group a { text-decoration: underline; }
    </style>
</head>
<body>
    
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="main-content">
        <div class="auth-container">
            <h2 class="auth-title">로그인</h2>
            
            <c:if test="${param.error == 'fail'}">
                <div class="error-msg">
                    ⚠️ 아이디 또는 비밀번호가 일치하지 않습니다.
                </div>
            </c:if>

            <form action="${ctx}/member/login" method="post">
                <div class="form-group">
                    <label class="form-label">아이디</label>
                    <input type="text" name="id" class="form-input" placeholder="아이디" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label">비밀번호</label>
                    <input type="password" name="password" class="form-input" placeholder="비밀번호" required>
                </div>
                
                <button type="submit" class="btn-primary btn-full">로그인</button>
            </form>
            
            <div class="link-group">
                아직 회원이 아니신가요? <a href="${ctx}/member/register">회원가입</a>
            </div>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>