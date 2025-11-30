<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지 - KeyHub</title>
<link rel="stylesheet" href="${ctx}/resources/css/reset.css">
<link rel="stylesheet" href="${ctx}/resources/css/variables.css">
<link rel="stylesheet" href="${ctx}/resources/css/layout.css">
<link rel="stylesheet" href="${ctx}/resources/css/components.css">
<link rel="stylesheet" href="${ctx}/resources/css/interactive.css">

<style>
    .mypage-container {
        display: flex;
        gap: 30px;
        margin-top: 40px;
    }

    /* 왼쪽: 프로필 카드 */
    .profile-card {
        flex: 1;
        background: white;
        padding: 30px;
        border-radius: var(--radius-md);
        border: 1px solid var(--border-color);
        text-align: center;
        height: fit-content;
    }

    .profile-img {
        width: 100px; height: 100px;
        background: #eee;
        border-radius: 50%;
        margin: 0 auto 20px;
        display: flex; align-items: center; justify-content: center;
        font-size: 40px; color: #ccc;
    }

    .profile-name {
        font-size: 20px; font-weight: 700; margin-bottom: 5px;
    }

    .profile-id {
        color: var(--text-sub); font-size: 14px; margin-bottom: 20px;
    }

    .profile-info {
        text-align: left; border-top: 1px solid #eee;
        padding-top: 20px;
    }

    .info-row {
        display: flex; justify-content: space-between;
        margin-bottom: 10px; font-size: 14px;
    }

    .info-label { color: var(--text-sub); }

    /* 오른쪽: 활동 내역 (대시보드) */
    .activity-section { flex: 3; }

    /* 리스트 스타일 */
    .recent-list {
        background: white; padding: 20px;
        border-radius: var(--radius-md);
        border: 1px solid var(--border-color);
        margin-bottom: 30px; /* 리스트 간 간격 추가 */
    }

    .list-header {
        font-weight: 700;
        margin-bottom: 15px;
        padding-bottom: 10px; border-bottom: 1px solid #eee;
        display: flex; justify-content: space-between; align-items: center;
    }

    .empty-msg {
        text-align: center; padding: 40px 0;
        color: var(--text-sub);
        font-size: 14px;
    }

    /* 리스트 아이템 공통 스타일 */
    .list-item {
        display: flex;
        align-items: center;
        padding: 15px 0; border-bottom: 1px solid #eee;
    }
    .list-item:last-child { border-bottom: none; }

    .item-img-box {
        width: 60px; height: 60px; border-radius: 4px;
        overflow: hidden; margin-right: 15px; border: 1px solid #eee;
        background: #f8f9fa;
    }
    .item-img-box img { width: 100%; height: 100%; object-fit: cover; }

    .item-info { flex: 1; }
    .item-title {
        font-weight: 700; font-size: 15px; display: block;
        margin-bottom: 5px; color: #333;
    }
    .item-price { color: var(--primary-color); font-weight: 600; }
    .item-meta { font-size: 12px; color: #888; margin-left: 10px; }
    
    /* 주문 상태 뱃지 */
    .status-badge {
        display: inline-block;
        padding: 4px 8px; border-radius: 4px; 
        font-size: 12px; font-weight: bold; color: white; background: #aaa;
    }
    .status-badge.done { background: var(--primary-color); }
</style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="container">
        <div class="section-header">
            <h3>마이페이지</h3>
        </div>
        
        <div class="mypage-container">
            <aside class="profile-card">
                <div class="profile-img">👤</div>
                <h4 class="profile-name">${sessionScope.user.name}님</h4>
                <p class="profile-id">@${sessionScope.user.id}</p>

                <div class="profile-info">
                    <div class="info-row">
                        <span class="info-label">이메일</span> 
                        <span>${sessionScope.user.email}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">가입일</span> 
                        <span><fmt:formatDate value="${sessionScope.user.joinDate}" pattern="yyyy-MM-dd" /></span>
                    </div>
                </div>

                <a href="${ctx}/member/edit" class="btn-primary" style="display:block;
                    width: 100%; margin-top: 20px; font-size: 14px; text-align:center; padding: 10px 0;">
                    내 정보 수정
                </a>            
            </aside>

            <section class="activity-section">
                
                <div class="recent-list">
                    <div class="list-header">
                        <span>🛒 나의 장바구니</span> 
                        <span style="font-size: 12px; color: #888;">총 ${myCartList.size()}개</span>
                    </div>

                    <c:choose>
                        <c:when test="${empty myCartList}">
                            <div class="empty-msg">장바구니가 비어있습니다.</div>
                        </c:when>
                        <c:otherwise>
                            <ul style="padding: 0 10px;">
                                <c:forEach var="cart" items="${myCartList}">
                                    <li class="list-item">
                                        <div class="item-img-box">
                                            <c:choose>
                                                <c:when test="${cart.productImg == 'no_image.png'}">
                                                    <img src="${ctx}/resources/images/common/no_image.png">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${ctx}/image?name=${cart.productImg}">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="item-info">
                                            <a href="${ctx}/market/detail?pNo=${cart.PNo}" class="item-title">
                                                ${cart.productTitle} 
                                            </a> 
                                            <span class="item-price">
                                                <fmt:formatNumber value="${cart.productPrice}" />원
                                            </span> 
                                            <span class="item-meta">판매자: ${cart.sellerId}</span>
                                        </div> 
                                        
                                        <a href="${ctx}/market/cart/delete?cNo=${cart.CNo}"
                                           onclick="return confirm('장바구니에서 삭제하시겠습니까?')"
                                           style="padding: 5px 10px; border: 1px solid #ddd; border-radius: 4px; font-size: 12px; color: #666;">
                                            삭제 
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="recent-list">
                    <div class="list-header">
                        <span>📦 구매 내역 (Order History)</span> 
                        <span style="font-size: 12px; color: #888;">총 ${myOrderList.size()}건</span>
                    </div>

                    <c:choose>
                        <c:when test="${empty myOrderList}">
                            <div class="empty-msg">구매한 상품이 없습니다.</div>
                        </c:when>
                        <c:otherwise>
                            <ul style="padding: 0 10px;">
                                <c:forEach var="order" items="${myOrderList}">
                                    <li class="list-item">
                                        <div class="item-img-box">
                                            <c:choose>
                                                <c:when test="${order.productImg == 'no_image.png'}">
                                                    <img src="${ctx}/resources/images/common/no_image.png">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${ctx}/image?name=${order.productImg}">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="item-info">
                                            <a href="${ctx}/market/detail?pNo=${order.pNo}" class="item-title">
                                                ${order.productTitle}
                                            </a> 
                                            <div style="font-size: 13px; color: #666;">
                                                <span style="color:var(--primary-color); font-weight:bold;">
                                                    <fmt:formatNumber value="${order.amount}" />원
                                                </span>
                                                <span style="margin: 0 5px; color:#ddd;">|</span>
                                                <span>주문일: <fmt:formatDate value="${order.orderDate}" pattern="yyyy.MM.dd"/></span>
                                            </div>
                                            <div style="font-size: 12px; color: #aaa; margin-top:4px;">
                                                배송지: ${order.address}
                                            </div>
                                        </div> 
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="recent-list">
                    <div class="list-header">
                        <span>🏷️ 판매 중인 상품</span> 
                        <span style="font-size: 12px; color: #888;">총 ${myProductList.size()}개</span>
                    </div>

                    <c:choose>
                        <c:when test="${empty myProductList}">
                            <div class="empty-msg">판매 중인 상품이 없습니다.</div>
                        </c:when>
                        <c:otherwise>
                            <ul style="padding: 0 10px;">
                                <c:forEach var="p" items="${myProductList}">
                                    <li class="list-item">
                                        <div class="item-img-box">
                                            <c:choose>
                                                <c:when test="${p.imgUrl == 'no_image.png'}">
                                                    <img src="${ctx}/resources/images/common/no_image.png">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${ctx}/image?name=${p.imgUrl}">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div class="item-info">
                                            <a href="${ctx}/market/detail?pNo=${p.pNo}" class="item-title">
                                                ${p.title}
                                            </a> 
                                            
                                            <div style="font-size: 13px; color: #666;">
                                                <span style="color:var(--primary-color); font-weight:bold;">
                                                    <fmt:formatNumber value="${p.price}" />원
                                                </span>
                                                <span style="margin: 0 5px; color:#ddd;">|</span>
                                                <span>등록일: <fmt:formatDate value="${p.regDate}" pattern="yyyy.MM.dd"/></span>
                                            </div>
                                            
                                            <div style="margin-top: 5px;">
                                                <c:choose>
                                                    <c:when test="${p.status == 'SOLD'}">
                                                        <span class="status-badge done">판매완료</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge" style="background:#4E54C8;">판매중</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div> 
                                        
                                        <div style="display:flex; flex-direction:column; gap:5px;">
                                            <a href="${ctx}/market/edit?pNo=${p.pNo}"
                                               style="padding: 4px 10px; border: 1px solid #ddd; border-radius: 4px; font-size: 12px; color: #333; text-align:center;">
                                                수정
                                            </a>
                                            <a href="${ctx}/market/delete?pNo=${p.pNo}"
                                               onclick="return confirm('정말 삭제하시겠습니까?')"
                                               style="padding: 4px 10px; border: 1px solid #ffdede; background:#fff0f0; border-radius: 4px; font-size: 12px; color: #d32f2f; text-align:center;">
                                                삭제
                                            </a>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>

            </section>
        </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>