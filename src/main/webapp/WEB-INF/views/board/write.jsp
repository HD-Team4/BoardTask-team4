<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 작성</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board.css">
    <script>
        function check() {
            if (!bbs.subject.value) { alert("제목을 입력하세요."); bbs.subject.focus(); return false; }
            if (!bbs.writer.value) { alert("작성자를 입력하세요."); bbs.writer.focus(); return false; }
            if (!bbs.pwd.value) { alert("비밀번호를 입력하세요."); bbs.pwd.focus(); return false; }
            document.bbs.submit();
        }
    </script>
</head>
<body>
<% pageContext.include("/WEB-INF/views/common/header.jsp"); %>
<main class="container board-shell">
    <section class="card board-card">
        <div class="card-header">
            <h2 class="page-title">게시글 작성</h2>
            <p class="page-subtitle">비회원형 게시판 기준으로 작성자와 비밀번호를 함께 저장합니다.</p>
        </div>
        <div class="card-body">
            <form name="bbs" action="<%=request.getContextPath()%>/board/write.do" method="POST" class="row g-4">
                <div class="col-12">
                    <label class="form-label">제목</label>
                    <input type="text" name="subject" class="form-control" placeholder="게시글 제목을 입력하세요">
                </div>
                <div class="col-md-6">
                    <label class="form-label">작성자</label>
                    <input type="text" name="writer" class="form-control" placeholder="이름">
                </div>
                <div class="col-md-6">
                    <label class="form-label">비밀번호</label>
                    <input type="password" name="pwd" class="form-control" placeholder="수정/삭제용 비밀번호">
                </div>
                <div class="col-md-6">
                    <label class="form-label">이메일</label>
                    <input type="email" name="email" class="form-control" placeholder="name@example.com">
                </div>
                <div class="col-md-6">
                    <label class="form-label">홈페이지</label>
                    <input type="text" name="homepage" class="form-control" value="http://">
                </div>
                <div class="col-12">
                    <label class="form-label">내용</label>
                    <textarea name="content" class="form-control" placeholder="내용을 입력하세요"></textarea>
                </div>
                <div class="col-12">
                    <label class="form-label">첨부 파일</label>
                    <input type="file" name="filename" class="form-control">
                </div>
                <div class="col-12 d-flex justify-content-end gap-2">
                    <a href="<%=request.getContextPath()%>/board/list.do" class="btn btn-outline-secondary rounded-pill px-4">목록</a>
                    <button type="reset" class="btn btn-light rounded-pill px-4">다시 작성</button>
                    <button type="button" class="btn btn-primary rounded-pill px-4" onclick="check();">등록</button>
                </div>
            </form>
        </div>
    </section>
</main>
<% pageContext.include("/WEB-INF/views/common/footer.jsp"); %>
</body>
</html>