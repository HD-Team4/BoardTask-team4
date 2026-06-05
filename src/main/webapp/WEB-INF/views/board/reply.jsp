<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("utf-8");
    String idx = request.getParameter("idx");
    String cpage = request.getParameter("cp");
    String pagesize = request.getParameter("ps");
    String subject = request.getParameter("subject");
    if (idx == null || subject == null || idx.trim().equals("") || subject.trim().equals("")) {
        response.sendRedirect(request.getContextPath() + "/board/list.do");
        return;
    }
    if (cpage == null || pagesize == null) { cpage = "1"; pagesize = "5"; }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>답글 작성</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board.css">
    <script>
        function boardcheck() {
            if (!bbs.subject.value) { alert("제목을 입력하세요."); bbs.subject.focus(); return false; }
            if (!bbs.writer.value) { alert("작성자를 입력하세요."); bbs.writer.focus(); return false; }
            if (!bbs.content.value) { alert("내용을 입력하세요."); bbs.content.focus(); return false; }
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
            <h2 class="page-title">답글 작성</h2>
            <p class="page-subtitle">원글 제목: <strong><%= subject %></strong></p>
        </div>
        <div class="card-body">
            <form name="bbs" action="<%=request.getContextPath()%>/board/reply.do" method="POST" class="row g-4">
                <input type="hidden" name="cp" value="<%= cpage %>">
                <input type="hidden" name="ps" value="<%= pagesize %>">
                <input type="hidden" name="idx" value="<%= idx %>">
                <div class="col-12">
                    <label class="form-label">제목</label>
                    <input type="text" name="subject" class="form-control" value="RE: <%= subject %>">
                </div>
                <div class="col-md-6"><label class="form-label">작성자</label><input type="text" name="writer" class="form-control"></div>
                <div class="col-md-6"><label class="form-label">비밀번호</label><input type="password" name="pwd" class="form-control"></div>
                <div class="col-md-6"><label class="form-label">이메일</label><input type="email" name="email" class="form-control"></div>
                <div class="col-md-6"><label class="form-label">홈페이지</label><input type="text" name="homepage" class="form-control" value="http://"></div>
                <div class="col-12"><label class="form-label">내용</label><textarea name="content" class="form-control"></textarea></div>
                <div class="col-12"><label class="form-label">첨부 파일</label><input type="file" name="filename" class="form-control"></div>
                <div class="col-12 d-flex justify-content-end gap-2">
                    <a href="<%=request.getContextPath()%>/board/list.do?cp=<%=cpage%>&ps=<%=pagesize%>" class="btn btn-outline-secondary rounded-pill px-4">목록</a>
                    <button type="reset" class="btn btn-light rounded-pill px-4">다시 작성</button>
                    <button type="button" class="btn btn-success rounded-pill px-4" onclick="boardcheck();">답글 등록</button>
                </div>
            </form>
        </div>
    </section>
</main>
<% pageContext.include("/WEB-INF/views/common/footer.jsp"); %>
</body>
</html>