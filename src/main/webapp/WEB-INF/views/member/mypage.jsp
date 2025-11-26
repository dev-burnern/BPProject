<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	width: 100px;
	height: 100px;
	background: #eee;
	border-radius: 50%;
	margin: 0 auto 20px;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 40px;
	color: #ccc;
}

.profile-name {
	font-size: 20px;
	font-weight: 700;
	margin-bottom: 5px;
}

.profile-id {
	color: var(--text-sub);
	font-size: 14px;
	margin-bottom: 20px;
}

.profile-info {
	text-align: left;
	border-top: 1px solid #eee;
	padding-top: 20px;
}

.info-row {
	display: flex;
	justify-content: space-between;
	margin-bottom: 10px;
	font-size: 14px;
}

.info-label {
	color: var(--text-sub);
}

/* 오른쪽: 활동 내역 (대시보드) */
.activity-section {
	flex: 3;
}

.dashboard-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 20px;
	margin-bottom: 30px;
}

.stat-card {
	background: white;
	padding: 20px;
	border-radius: var(--radius-md);
	border: 1px solid var(--border-color);
	text-align: center;
}

.stat-number {
	font-size: 24px;
	font-weight: 800;
	color: var(--primary-color);
	display: block;
}

.stat-label {
	font-size: 14px;
	color: var(--text-sub);
}

.recent-list {
	background: white;
	padding: 20px;
	border-radius: var(--radius-md);
	border: 1px solid var(--border-color);
}

.list-header {
	font-weight: 700;
	margin-bottom: 15px;
	padding-bottom: 10px;
	border-bottom: 1px solid #eee;
}

.empty-msg {
	text-align: center;
	padding: 40px 0;
	color: var(--text-sub);
	font-size: 14px;
}
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
						<span class="info-label">이메일</span> <span>${sessionScope.user.email}</span>
					</div>
					<div class="info-row">
						<span class="info-label">가입일</span> <span> <fmt:formatDate
								value="${sessionScope.user.joinDate}" pattern="yyyy-MM-dd" />
						</span>
					</div>
				</div>

				<a href="${ctx}/member/edit" class="btn-primary" style="display:block; width: 100%; margin-top: 20px; font-size: 14px; text-align:center; padding: 10px 0;">
				    내 정보 수정
				</a>			
			</aside>

			<section class="activity-section">
				<div class="dashboard-grid">
					<div class="stat-card">
						<span class="stat-number">${myCartList.size()}</span> <span class="stat-label">장바구니</span>
					</div>
					<div class="stat-card">
						<span class="stat-number">${productCount}</span> <span class="stat-label">판매
							내역</span>
					</div>
					<div class="stat-card">
						<span class="stat-number">${boardCount}</span> <span
							class="stat-label">작성 글</span>
					</div>
				</div>
				<div class="recent-list">
					<div class="list-header"
						style="display: flex; justify-content: space-between;">
						<span>🛒 나의 장바구니</span> <span
							style="font-size: 12px; color: #888;">총
							${myCartList.size()}개</span>
					</div>

					<c:choose>
						<c:when test="${empty myCartList}">
							<div class="empty-msg">장바구니가 비어있습니다.</div>
						</c:when>
						<c:otherwise>
							<ul style="padding: 0 10px;">
								<c:forEach var="cart" items="${myCartList}">
									<li
										style="display: flex; align-items: center; padding: 15px 0; border-bottom: 1px solid #eee;">
										<div
											style="width: 60px; height: 60px; border-radius: 4px; overflow: hidden; margin-right: 15px; border: 1px solid #eee;">
											<c:choose>
												<c:when test="${cart.productImg == 'no_image.png'}">
													<img src="${ctx}/resources/images/common/no_image.png"
														style="width: 100%; height: 100%; object-fit: cover;">
												</c:when>
												<c:otherwise>
													<img src="${ctx}/image?name=${cart.productImg}"
														style="width: 100%; height: 100%; object-fit: cover;">
												</c:otherwise>
											</c:choose>
										</div>

										<div style="flex: 1;">
											<a href="${ctx}/market/detail?pNo=${cart.PNo}"
												style="font-weight: 700; font-size: 15px; display: block; margin-bottom: 5px;">
												${cart.productTitle} </a> <span
												style="color: var(--primary-color); font-weight: 600;">
												<fmt:formatNumber value="${cart.productPrice}" />원
											</span> <span
												style="font-size: 12px; color: #888; margin-left: 10px;">판매자:
												${cart.sellerId}</span>
										</div> <a href="${ctx}/market/cart/delete?cNo=${cart.CNo}"
										onclick="return confirm('삭제하시겠습니까?')"
										style="padding: 5px 10px; border: 1px solid #ddd; border-radius: 4px; font-size: 12px; color: #666;">
											삭제 </a>
									</li>
								</c:forEach>
							</ul>
						</c:otherwise>
					</c:choose>
				</div>
	</main>

	<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>