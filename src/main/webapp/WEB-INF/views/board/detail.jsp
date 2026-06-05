<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 상세</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />
<main class="container board-shell">
    <!-- 1. 게시글 상세 카드 -->
    <section class="card board-card mb-4">
        <div class="card-header">
            <div class="d-flex flex-column flex-lg-row justify-content-between gap-3">
                <div>
                    <h2 class="page-title"><c:out value="${board.title}"/></h2>
                    <p class="page-subtitle">
                        #${board.boardId} · <c:out value="${board.writer}"/> · ${board.createdAt}
                    </p>
                </div>
                <div class="d-flex flex-wrap align-items-center gap-2">
                    <span class="meta-pill">조회 <strong>${board.readCount}</strong></span>
                </div>
            </div>
        </div>
        <div class="card-body">
            <!-- 줄바꿈 처리를 위해 fn:replace 사용 또는 CSS의 white-space: pre-wrap 적용 -->
            <div class="detail-content" style="white-space: pre-wrap;"><c:out value="${board.content}"/></div>
            
            <div class="d-flex flex-wrap justify-content-end gap-2 mt-4">
                <a class="btn btn-outline-secondary rounded-pill px-4" href="${pageContext.request.contextPath}/board/list.do?cp=${cp}&ps=${ps}">목록</a>
                <a class="btn btn-outline-primary rounded-pill px-4" href="${pageContext.request.contextPath}/board/modifyForm.do?boardId=${board.boardId}&cp=${cp}&ps=${ps}">수정</a>
                <form action="${pageContext.request.contextPath}/board/delete.do" method="POST" class="d-inline-flex gap-2">
                    <input type="hidden" name="boardId" value="${board.boardId}">
                    <input type="hidden" name="cp" value="${cp}">
                    <input type="hidden" name="ps" value="${ps}">
                    <input type="password" name="password" class="form-control form-control-sm" placeholder="비밀번호" required>
                    <button type="submit" class="btn btn-outline-danger rounded-pill px-4">삭제</button>
                </form>
                <a class="btn btn-success rounded-pill px-4" href="${pageContext.request.contextPath}/board/replyForm.do?boardId=${board.boardId}&cp=${cp}&ps=${ps}">답글</a>
            </div>
        </div>
    </section>

    <!-- 2. 댓글 카드 (비동기 Ajax 영역) -->
    <section class="card board-card mb-5">
        <div class="card-header">
            <h3 class="page-title fs-4">댓글</h3>
            <p class="page-subtitle">비동기로 작성 및 관리되는 영역입니다.</p>
        </div>
        <div class="card-body">
            <!-- 댓글 등록 폼 (비동기 처리용) -->
            <form id="commentForm" class="comment-box row g-3 mb-4">
                <input type="hidden" id="commentBoardId" name="boardId" value="${board.boardId}">
                <div class="col-md-3">
                    <input type="text" id="commentWriter" name="writer" class="form-control" placeholder="작성자" required>
                </div>
                <div class="col-md-2">
                    <input type="password" id="commentPassword" name="password" class="form-control" placeholder="비밀번호" required>
                </div>
                <div class="col-md-7 d-flex gap-2">
                    <input type="text" id="commentContent" name="content" class="form-control" placeholder="댓글 내용을 입력하세요" required>
                    <button type="submit" class="btn btn-primary px-4">등록</button>
                </div>
            </form>

            <!-- 댓글 리스트가 동적으로 그려질 영역 -->
            <div id="commentList" class="comment-list">
                <div class="text-center text-muted py-4">댓글을 불러오는 중...</div>
            </div>
        </div>
    </section>
</main>

<!-- 댓글 수정용 모달 다이얼로그 (Bootstrap 5) -->
<div class="modal fade" id="commentEditModal" tabindex="-1" aria-labelledby="commentEditModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="commentEditModalLabel">댓글 수정</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="commentEditForm">
                    <input type="hidden" id="editCommentId" name="commentId">
                    <div class="mb-3">
                        <label for="editCommentWriter" class="form-label">작성자</label>
                        <input type="text" class="form-control" id="editCommentWriter" name="writer" required>
                    </div>
                    <div class="mb-3">
                        <label for="editCommentContent" class="form-label">내용</label>
                        <textarea class="form-control" id="editCommentContent" name="content" rows="3" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="editCommentPassword" class="form-label">비밀번호 확인</label>
                        <input type="password" class="form-control" id="editCommentPassword" name="password" placeholder="비밀번호를 입력하세요" required>
                    </div>
                    <div class="d-flex justify-content-end gap-2">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                        <button type="submit" class="btn btn-primary">수정 완료</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 댓글 삭제용 모달 다이얼로그 (Bootstrap 5) -->
<div class="modal fade" id="commentDeleteModal" tabindex="-1" aria-labelledby="commentDeleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="commentDeleteModalLabel">댓글 삭제</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="commentDeleteForm">
                    <input type="hidden" id="deleteCommentId" name="commentId">
                    <div class="mb-3">
                        <p class="text-danger">댓글을 삭제하시려면 비밀번호를 입력해주세요.</p>
                        <label for="deleteCommentPassword" class="form-label">비밀번호</label>
                        <input type="password" class="form-control" id="deleteCommentPassword" name="password" required>
                    </div>
                    <div class="d-flex justify-content-end gap-2">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                        <button type="submit" class="btn btn-danger">삭제 완료</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<c:import url="/WEB-INF/views/common/footer.jsp" />

<!-- Context Path를 JS에서 사용할 수 있도록 변수 제공 -->
<script>
    var contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/comment.js"></script>
</body>
</html>