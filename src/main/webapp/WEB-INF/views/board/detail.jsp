<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>${board.title} - KeyHub</title>
    <link rel="stylesheet" href="${ctx}/resources/css/reset.css">
    <link rel="stylesheet" href="${ctx}/resources/css/variables.css">
    <link rel="stylesheet" href="${ctx}/resources/css/layout.css">
    <link rel="stylesheet" href="${ctx}/resources/css/components.css">
    <link rel="stylesheet" href="${ctx}/resources/css/interactive.css">
    
    <style>
        /* 1. 게시글 본문 스타일 */
        .board-view { 
            max-width: 900px; margin: 0 auto; 
            border: 1px solid var(--border-color); border-radius: var(--radius-md); 
            padding: 30px; background: white;
        }
        .view-header { border-bottom: 1px solid #eee; padding-bottom: 20px; margin-bottom: 30px; }
        .view-title { font-size: 24px; font-weight: 700; margin-bottom: 15px; }
        .view-meta { color: var(--text-sub); font-size: 14px; display: flex; gap: 20px; }
        
        .view-content { 
            min-height: 200px; line-height: 1.8; 
            white-space: pre-wrap; /* 줄바꿈 보존 */
            font-size: 16px; color: var(--text-main);
        }

        /* 2. 댓글 영역 전체 레이아웃 */
        .reply-section { margin-top: 50px; border-top: 2px solid #eee; padding-top: 30px; }
        
        /* 3. 댓글 입력 폼 스타일  */
        .reply-form-container {
            display: flex; gap: 8px; margin-bottom: 30px; align-items: flex-start;
        }
        .reply-textarea {
            flex: 1; min-height: 52px; padding: 12px 15px;
            border: 1px solid #ddd; border-radius: 4px;
            font-family: inherit; font-size: 14px; resize: vertical;
            transition: border-color 0.2s;
        }
        .reply-textarea:focus { border-color: var(--primary-color); outline: none; }
        
        .btn-reply-submit {
            width: 80px; height: 52px; 
            background-color: var(--primary-color); color: white;
            border: none; border-radius: 4px;
            font-weight: 700; font-size: 14px; cursor: pointer;
            transition: background-color 0.2s;
        }
        .btn-reply-submit:hover { background-color: var(--primary-dark); }

        /* 4. 댓글 리스트 스타일 */
        .reply-list { list-style: none; padding: 0; }
        .reply-item { padding: 20px 10px; border-bottom: 1px solid #f1f1f1; }
        
        /* 대댓글 들여쓰기 */
        .reply-item.nested { 
            margin-left: 40px; background: #f8f9fa; border-radius: 8px; 
            margin-top: 10px; border: none;
        }

        /* 댓글 헤더 (작성자 + 버튼 가로 배치) */
        .reply-header {
            display: flex; justify-content: space-between; align-items: center;
            margin-bottom: 10px;
        }
        .reply-meta { display: flex; align-items: center; gap: 10px; }
        .reply-writer { font-weight: 700; font-size: 15px; color: #333; }
        .reply-date { font-size: 12px; color: #999; }

        /* 댓글 미니 버튼 그룹 */
        .reply-actions { display: flex; align-items: center; gap: 6px; }
        .btn-mini {
            font-size: 12px; padding: 4px 10px; height: 26px;
            border: 1px solid #ddd; background-color: white; color: #666;
            border-radius: 4px; cursor: pointer; text-decoration: none;
            display: flex; align-items: center; justify-content: center;
            transition: all 0.2s;
        }
        .btn-mini:hover { background-color: #f5f5f5; border-color: #bbb; color: #333; }
        .btn-mini.del { color: #e74c3c; border-color: #fadbd8; }
        .btn-mini.del:hover { background-color: #fff5f5; border-color: #e74c3c; }
        
        /* 5. 하단 목록/수정/삭제 버튼 그룹 (통일됨) */
        .btn-container {
            display: flex; justify-content: center; gap: 10px;
            margin-top: 40px; padding-bottom: 20px;
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
        <div class="board-view">
            <div class="view-header">
                <h2 class="view-title">${board.title}</h2>
                <div class="view-meta">
                    <span>작성자: <b>${board.writerId}</b></span>
                    <span>작성일: <fmt:formatDate value="${board.regDate}" pattern="yyyy-MM-dd HH:mm"/></span>
                    <span>조회수: ${board.views}</span>
                </div>
            </div>
            
            <div class="view-content">${board.content}</div>
            
            <div class="reply-section">
                <h3 style="margin-bottom: 20px; font-size: 18px; font-weight: 700;">
                    댓글 <span style="color:var(--primary-color);">${replyList.size()}</span>
                </h3>
                
                <c:if test="${not empty sessionScope.user}">
                    <form action="${ctx}/reply/add" method="post" class="reply-form-container">
                        <input type="hidden" name="bNo" value="${board.bNo}">
                        
                        <textarea name="content" class="reply-textarea" 
                                  placeholder="댓글을 남겨보세요 (타인을 배려하는 마음을 담아주세요)" required></textarea>
                        
                        <button type="submit" class="btn-reply-submit">등록</button>
                    </form>
                </c:if>

                <ul class="reply-list">
                    <c:forEach var="r" items="${replyList}">
                        <li class="reply-item ${r.parentRNo != 0 ? 'nested' : ''}">
                            
                            <div class="reply-header">
                                <div class="reply-meta">
                                    <c:if test="${r.parentRNo != 0}">
                                        <span style="color:#aaa;">↳</span>
                                    </c:if> 
                                    <span class="reply-writer">${r.writerId}</span>
                                    <span class="reply-date">
                                        <fmt:formatDate value="${r.regDate}" pattern="yyyy-MM-dd HH:mm"/>
                                    </span>
                                </div>
                                
                                <div class="reply-actions">
                                    <c:if test="${not empty sessionScope.user && r.parentRNo == 0}">
                                        <button onclick="toggleReplyForm('${r.rNo}')" class="btn-mini">답글</button>
                                    </c:if>
                                    
                                    <c:if test="${not empty sessionScope.user && sessionScope.user.id == r.writerId}">
                                        <a href="${ctx}/reply/delete?rNo=${r.rNo}&bNo=${board.bNo}" 
                                           onclick="return confirm('정말 삭제하시겠습니까?')"
                                           class="btn-mini del">삭제</a>
                                    </c:if>
                                </div>
                            </div>
                            
                            <div style="font-size:15px; line-height:1.6; color:#444; padding-left: ${r.parentRNo != 0 ? '15px' : '0'};">
                                <c:out value="${r.content}" />
                            </div>

                            <div id="reply-form-${r.rNo}" style="display:none; margin-top:15px; padding-top:15px; border-top:1px dashed #ddd;">
                                <form action="${ctx}/reply/add" method="post" style="display:flex; gap:8px;">
                                    <input type="hidden" name="bNo" value="${board.bNo}">
                                    <input type="hidden" name="parentRNo" value="${r.rNo}">
                                    
                                    <input type="text" name="content" placeholder="@${r.writerId}님에게 답글 작성..." required 
                                           style="flex:1; padding:8px 12px; border:1px solid #ddd; border-radius:4px; font-family:inherit;">
                                    
                                    <button type="submit" class="btn-primary" style="font-size:13px; padding:0 15px; height:35px;">등록</button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                    
                    <c:if test="${empty replyList}">
                        <li style="text-align:center; padding:40px; color:#999;">첫 번째 댓글을 남겨주세요!</li>
                    </c:if>
                </ul>
            </div>

            <div class="btn-container">
                <a href="${ctx}/board/list" class="action-btn btn-list">목록으로</a>
                
                <c:if test="${not empty sessionScope.user and sessionScope.user.id == board.writerId}">
                    <a href="${ctx}/board/update?bNo=${board.bNo}" class="action-btn btn-modify">수정</a>
                    <a href="javascript:deleteBoard('${board.bNo}')" class="action-btn btn-delete">삭제</a>
                </c:if>
            </div>
        </div>
    </main>

    <script>
        // 게시글 삭제 확인
        function deleteBoard(bNo) {
            if (confirm("정말로 이 글을 삭제하시겠습니까?\n삭제 후에는 복구할 수 없습니다.")) {
                location.href = '${ctx}/board/delete?bNo=' + bNo;
            }
        }

        // 대댓글 폼 토글
        function toggleReplyForm(rNo) {
            const form = document.getElementById('reply-form-' + rNo);
            if (form.style.display === 'none') {
                form.style.display = 'block';
                form.querySelector('input[name="content"]').focus(); // 입력창 자동 포커스
            } else {
                form.style.display = 'none';
            }
        }
    </script>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>