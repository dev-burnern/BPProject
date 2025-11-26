<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${product.title} - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
    
    <style>
        .detail-container {
            display: flex;
            gap: 40px;
            margin-top: 40px;
        }
        
        /* 왼쪽: 이미지 영역 */
        .detail-img-box {
            flex: 1;
            border-radius: var(--radius-md);
            overflow: hidden;
            border: 1px solid var(--border-color);
            height: 400px;
            display: flex; align-items: center; justify-content: center;
            background: #f8f9fa;
        }
        .detail-img-box img {
            max-width: 100%; max-height: 100%;
            object-fit: contain;
        }
        
        /* 오른쪽: 정보 영역 */
        .detail-info-box {
            flex: 1;
            padding: 10px;
        }
        .detail-status {
            display: inline-block; padding: 4px 8px; font-size: 13px;
            color: white; border-radius: 4px; margin-bottom: 10px;
        }
        
        .detail-title {
            font-size: 28px; font-weight: 800; margin-bottom: 15px;
            line-height: 1.3;
        }
        .detail-price {
            font-size: 32px; font-weight: 800; color: var(--primary-color);
            margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 20px;
        }
        .detail-meta {
            color: var(--text-sub); font-size: 14px; margin-bottom: 30px;
        }
        .detail-meta span { margin-right: 15px; }
        
        .detail-content {
            min-height: 150px;
            line-height: 1.6; color: var(--text-main);
            margin-bottom: 40px;
            white-space: pre-wrap; /* 줄바꿈 유지 */
        }
        
        /* 구매/장바구니 버튼 그룹 */
        .btn-group { display: flex; gap: 10px; }
        .btn-buy { flex: 2; height: 50px; font-size: 18px; }
        .btn-cart { flex: 1; background: white; border: 1px solid var(--border-color); color: var(--text-main); }
        .btn-cart:hover { border-color: var(--primary-color); color: var(--primary-color); }

        /* 하단 목록/수정/삭제 버튼 컨테이너  */
        .btn-container {
            display: flex; justify-content: center; gap: 10px;
            margin-top: 40px; padding-bottom: 40px;
        }
        .action-btn {
            display: inline-flex; align-items: center; justify-content: center;
            min-width: 80px; height: 40px; padding: 0 20px;
            font-size: 14px; font-weight: 500; border-radius: 4px;
            transition: all 0.2s; border: 1px solid transparent; cursor: pointer;
        }
        .btn-list { background-color: white; border-color: #ddd; color: #555; }
        .btn-list:hover { background-color: #f5f5f5; border-color: #ccc; }
        
        .btn-modify { background-color: var(--primary-color); color: white; }
        .btn-modify:hover { background-color: var(--primary-dark); }
        
        .btn-delete { background-color: #ff4d4f; color: white; }
        .btn-delete:hover { background-color: #d9363e; }
    </style>
</head>
<body>
    
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="detail-container">
            
            <div class="detail-img-box">
                <c:choose>
                    <c:when test="${product.imgUrl == 'no_image.png'}">
                        <img src="https://placehold.co/600x400/e9ecef/adb5bd?text=No+Image" alt="상품이미지">
                    </c:when>
                    <c:otherwise>
                        <img src="${ctx}/image?name=${product.imgUrl}" alt="상품이미지">
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="detail-info-box">
                
                <c:choose>
                    <c:when test="${product.status == 'SOLD'}">
                        <span class="detail-status" style="background-color:#aaa;">판매완료</span>
                    </c:when>
                    <c:otherwise>
                        <span class="detail-status" style="background-color:var(--primary-color);">판매중</span>
                    </c:otherwise>
                </c:choose>
                
                <h2 class="detail-title">${product.title}</h2>
                
                <div class="detail-price">
                    <fmt:formatNumber value="${product.price}" type="currency" />
                </div>
                
                <div class="detail-meta">
                    <span>판매자: <b>${product.sellerId}</b></span>
                    <span>등록일: <fmt:formatDate value="${product.regDate}" pattern="yyyy-MM-dd"/></span>
                </div>
                
                <div class="detail-content">
                    ${product.content}
                </div>
                
                <div class="btn-group">
                    <c:choose>
                        <c:when test="${product.status == 'SOLD'}">
                            <button class="btn-primary btn-buy" style="background:#ccc; cursor:not-allowed;" disabled>판매완료된 상품입니다</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn-primary btn-buy" onclick="handleBuy()">구매하기</button>
                            <button class="btn-primary btn-cart" onclick="handleCart()">장바구니</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        
        <div class="btn-container">
            <a href="${ctx}/market/list" class="action-btn btn-list">목록으로</a>
            
            <c:if test="${not empty sessionScope.user and sessionScope.user.id == product.sellerId}">
                 <a href="${ctx}/market/edit?pNo=${product.pNo}" class="action-btn btn-modify">수정</a>
                 
                 <a href="javascript:deleteProduct('${product.pNo}')" class="action-btn btn-delete">삭제</a>
            </c:if>
        </div>
    </main>
    
    <script>
        // 로그인 상태 확인
        const isLoggedIn = ${not empty sessionScope.user};
        const loginUrl = "${ctx}/member/login";

        // 공통 로그인 체크 함수
        function checkLogin() {
            if (!isLoggedIn) {
                if (confirm("로그인이 필요한 서비스입니다.\n로그인 페이지로 이동하시겠습니까?")) {
                    location.href = loginUrl;
                }
                return false;
            }
            return true;
        }

        // 구매하기 버튼 클릭
        function handleBuy() {
            if (checkLogin()) {
                // 가상 결제 페이지로 이동
                location.href = '${ctx}/market/checkout?pNo=${product.pNo}';
            }
        }

        // 장바구니 버튼 클릭
        function handleCart() {
            if (checkLogin()) {
                if (confirm("상품을 장바구니에 담으시겠습니까?\n(담은 후 마이페이지로 이동합니다)")) {
                    // 장바구니 담기 요청
                    location.href = '${ctx}/market/cart/add?pNo=${product.pNo}'; 
                }
            }
        }

        // 상품 삭제 확인
        function deleteProduct(pNo) {
            if (confirm("정말로 이 상품을 삭제하시겠습니까?\n삭제 후에는 복구할 수 없습니다.")) {
                location.href = '${ctx}/market/delete?pNo=' + pNo;
            }
        }
    </script>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>