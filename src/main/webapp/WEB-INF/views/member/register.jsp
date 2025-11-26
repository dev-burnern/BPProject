<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
    
    <style>
        /* 회원가입 전용 간단 스타일 */
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
        .btn-full { width: 100%; text-align: center; margin-top: 10px; cursor: pointer;}
    </style>
</head>
<body>
    
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="main-content">
        <div class="auth-container">
            <h2 class="auth-title">회원가입</h2>
            
            <form action="${ctx}/member/register" method="post">
                <div class="form-group">
                    <label class="form-label">아이디</label>
                    <input type="text" name="id" class="form-input" placeholder="아이디 입력" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label">비밀번호</label>
                    <input type="password" name="password" class="form-input" placeholder="비밀번호 (4자 이상)" required minlength="4">
                </div>
                
                <div class="form-group">
                    <label class="form-label">이름</label>
                    <input type="text" name="name" class="form-input" placeholder="실명 입력" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label">이메일</label>
                    <input type="email" name="email" class="form-input" placeholder="example@keyhub.com" required>
                </div>
                
                <button type="submit" class="btn-primary btn-full">가입하기</button>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>