<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 수정</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board.css">
    <script>
        function validateEditForm() {
            var form = document.editForm;
            if (!form.title.value.trim()) {
                alert("제목을 입력하세요.");
                form.title.focus();
                return false;
            }
            if (!form.writer.value.trim()) {
                alert("작성자를 입력하세요.");
                form.writer.focus();
                return false;
            }
            if (!form.password.value.trim()) {
                alert("비밀번호를 입력하세요.");
                form.password.focus();
                return false;
            }
            if (!form.content.value.trim()) {
                alert("내용을 입력하세요.");
                form.content.focus();
                return false;
            }
            form.submit();
        }
    </script>
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />
<main class="container board-shell">
    <section class="card board-card">
        <div class="card-header">
            <h2 class="page-title">게시글 수정</h2>
            <p class="page-subtitle">게시글 #${board.boardId} · 작성일 ${board.createdAt}</p>
        </div>
        <div class="card-body">
            <form name="editForm" action="${pageContext.request.contextPath}/board/modify.do" method="POST" class="row g-4">
                <input type="hidden" name="boardId" value="${board.boardId}">
                <input type="hidden" name="cp" value="${cp}">
                <input type="hidden" name="ps" value="${ps}">
                
                <div class="col-12">
                    <label for="title" class="form-label fw-bold">제목</label>
                    <input type="text" id="title" name="title" class="form-control" value="<c:out value="${board.title}"/>" required>
                </div>
                <div class="col-md-6">
                    <label for="writer" class="form-label fw-bold">작성자</label>
                    <input type="text" id="writer" name="writer" class="form-control" value="<c:out value="${board.writer}"/>" required>
                </div>
                <div class="col-md-6">
                    <label for="password" class="form-label fw-bold">비밀번호 확인</label>
                    <input type="password" id="password" name="password" class="form-control" placeholder="기존 글 비밀번호를 입력해 주세요" required>
                </div>
                <div class="col-12">
                    <label for="content" class="form-label fw-bold">내용</label>
                    <textarea id="content" name="content" class="form-control" rows="8" required><c:out value="${board.content}"/></textarea>
                </div>
                <div class="col-12 d-flex justify-content-end gap-2">
                    <a href="${pageContext.request.contextPath}/board/detail.do?boardId=${board.boardId}&cp=${cp}&ps=${ps}" class="btn btn-outline-secondary rounded-pill px-4">취소</a>
                    <button type="reset" class="btn btn-light rounded-pill px-4">초기화</button>
                    <button type="button" class="btn btn-primary rounded-pill px-4" onclick="validateEditForm()">수정 저장</button>
                </div>
            </form>
        </div>
    </section>
</main>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>