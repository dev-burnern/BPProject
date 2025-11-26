<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>상품 등록 - KeyHub</title>
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
        
        /* 파일 업로드 스타일 */
        .file-box { margin-top: 10px; }
    </style>
</head>
<body>
    
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="section-header">
            <h3>키보드 판매하기</h3>
        </div>

        <div class="write-container">
            <!--  파일 업로드를 위해 enctype 필수 설정 -->
            <form action="${ctx}/market/write" method="post" enctype="multipart/form-data">
                
                <!-- 카테고리 선택 추가 -->
                <div class="form-row">
                    <label class="form-label">카테고리</label>
                    <select name="category" class="form-input-full" style="cursor:pointer;">
                        <option value="keyboard">키보드 (완제품)</option>
                        <option value="keycap">키캡</option>
                        <option value="switch">스위치</option>
                        <option value="etc">기타 용품</option>
                    </select>
                </div>

                <div class="form-row">
                    <label class="form-label">제목</label>
                    <input type="text" name="title" class="form-input-full" placeholder="상품명을 입력해주세요 (예: 리얼포스 R3)" required>
                </div>

                <div class="form-row">
                    <label class="form-label">가격</label>
                    <input type="number" name="price" class="form-input-full" placeholder="가격을 입력해주세요 (숫자만)" required>
                </div>

                <div class="form-row">
                    <label class="form-label">상품 이미지</label>
                    <input type="file" name="file" class="form-input-full" accept="image/*">
                    <p style="font-size: 12px; color: #888; margin-top: 5px;">* jpg, png 파일만 업로드 가능합니다.</p>
                </div>

                <div class="form-row">
                    <label class="form-label">상세 설명</label>
                    <textarea name="content" class="form-input-full" placeholder="상품의 상태, 구매 시기 등 상세 정보를 적어주세요."></textarea>
                </div>

                <div style="text-align: center; margin-top: 30px;">
                    <button type="submit" class="btn-primary" style="width: 200px;">등록완료</button>
                    <a href="${ctx}/market/list" class="btn-secondary" style="margin-left: 10px; color: #666;">취소</a>
                </div>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>