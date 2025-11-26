<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>주문/결제 - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    
    <style>
        .checkout-container { max-width: 600px; margin: 40px auto; padding: 30px; background: white; border: 1px solid #ddd; border-radius: 8px; }
        .order-summary { display: flex; gap: 20px; margin-bottom: 30px; padding-bottom: 20px; border-bottom: 1px solid #eee; }
        .order-img { width: 100px; height: 100px; border-radius: 4px; background: #f8f9fa; overflow: hidden; }
        .order-img img { width: 100%; height: 100%; object-fit: cover; }
        .order-info h4 { margin-bottom: 10px; font-size: 18px; }
        .order-price { font-size: 20px; font-weight: 700; color: var(--primary-color); }
        
        .form-group { margin-bottom: 20px; }
        .form-label { display: block; font-weight: 600; margin-bottom: 8px; }
        .form-input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; }
        
        .btn-pay { width: 100%; height: 50px; font-size: 18px; background: var(--primary-color); color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: 700; }
        .btn-pay:hover { background: var(--primary-dark); }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="section-header"><h3>주문/결제</h3></div>
        
        <div class="checkout-container">
            <div class="order-summary">
                <div class="order-img">
                    <c:choose>
                        <c:when test="${product.imgUrl == 'no_image.png'}">
                            <img src="${ctx}/resources/images/common/no_image.png">
                        </c:when>
                        <c:otherwise>
                            <img src="${ctx}/image?name=${product.imgUrl}">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="order-info">
                    <h4>${product.title}</h4>
                    <div class="order-price"><fmt:formatNumber value="${product.price}" />원</div>
                    <p style="color:#888; font-size:13px; margin-top:5px;">판매자: ${product.sellerId}</p>
                </div>
            </div>
            
            <form action="${ctx}/market/buy" method="post" onsubmit="return confirm('가상 결제를 진행하시겠습니까?');">
                <input type="hidden" name="pNo" value="${product.pNo}">
                <input type="hidden" name="price" value="${product.price}">
                
                <div class="form-group">
                    <label class="form-label">받는 사람</label>
                    <input type="text" class="form-input" value="${sessionScope.user.name}" readonly style="background:#f5f5f5;">
                </div>
                
                <div class="form-group">
                    <label class="form-label">배송지 입력</label>
                    <input type="text" name="address" class="form-input" placeholder="상세 주소를 입력해주세요 (예: 서울시 강남구...)" required>
                </div>
                
                <div class="form-group">
                    <label class="form-label">결제 수단</label>
                    <select class="form-input">
                        <option>무통장 입금 (가상)</option>
                        <option>신용카드 (가상)</option>
                    </select>
                </div>
                
                <button type="submit" class="btn-pay">
                    <fmt:formatNumber value="${product.price}" />원 결제하기
                </button>
            </form>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>