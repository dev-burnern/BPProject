<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>상품 수정 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    
    <style>
        .write-container {
            max-width: 800px; margin: 40px auto; padding: 40px;
            background: white; border-radius: var(--radius-md);
            border: 1px solid var(--border-color);
        }
        .form-row { margin-bottom: 20px; }
        .form-label { display: block; font-weight: 700; margin-bottom: 10px; }
        .form-input-full { width: 100%; padding: 12px; border: 1px solid var(--border-color); border-radius: 4px; }
        textarea.form-input-full { height: 200px; resize: vertical; }
        .current-img-box { margin-bottom: 10px; font-size: 14px; color: #666; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="section-header"><h3>상품 수정하기</h3></div>

        <div class="write-container">
            <form action="${ctx}/market/edit" method="post" enctype="multipart/form-data">
                <input type="hidden" name="pNo" value="${product.pNo}">
                <input type="hidden" name="originalImg" value="${product.imgUrl}">
                
                <div class="form-row">
                    <label class="form-label">제목</label>
                    <input type="text" name="title" class="form-input-full" value="${product.title}" required>
                </div>

                <div class="form-row">
                    <label class="form-label">가격</label>
                    <input type="number" name="price" class="form-input-full" value="${product.price}" required>
                </div>

                <div class="form-row">
                    <label class="form-label">상품 이미지</label>
                    <div class="current-img-box">
                        현재 파일: ${product.imgUrl} <br>
                        (파일을 선택하지 않으면 기존 이미지가 유지됩니다.)
                    </div>
                    <input type="file" name="file" class="form-input-full" accept="image/*">
                </div>

                <div class="form-row">
                    <label class="form-label">상세 설명</label>
                    <textarea name="content" class="form-input-full" required>${product.content}</textarea>
                </div>

                <div style="text-align: center; margin-top: 30px;">
                    <button type="submit" class="btn-primary">수정완료</button>
                    <a href="${ctx}/market/detail?pNo=${product.pNo}" class="btn-secondary" style="margin-left: 10px;">취소</a>
                </div>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>