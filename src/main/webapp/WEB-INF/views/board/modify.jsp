<%@page import="kr.or.bit.dto.BoardDTO"%>
<%@page import="kr.or.bit.service.BoardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String idx = request.getParameter("idx");
    if (idx == null || idx.trim().equals("")) {
        response.sendRedirect(request.getContextPath() + "/board/list.do");
        return;
    }
    BoardService service = BoardService.getInBoardService();
    BoardDTO board = service.board_EditContent(idx);
    if (board == null) {
        out.print("데이터를 찾을 수 없습니다.");
        out.print("<hr><a href='" + request.getContextPath() + "/board/list.do'>목록</a>");
        return;
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 수정</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board.css">
    <script>
        function editCheck() {
            if (!edit.writer.value) { alert("작성자를 입력하세요."); edit.writer.focus(); return false; }
            if (!edit.pwd.value) { alert("비밀번호를 입력하세요."); edit.pwd.focus(); return false; }
            if (!edit.subject.value) { alert("제목을 입력하세요."); edit.subject.focus(); return false; }
            if (!edit.content.value) { alert("내용을 입력하세요."); edit.content.focus(); return false; }
            document.edit.submit();
        }
    </script>
</head>
<body>
<% pageContext.include("/WEB-INF/views/common/header.jsp"); %>
<main class="container board-shell">
    <section class="card board-card">
        <div class="card-header">
            <h2 class="page-title">게시글 수정</h2>
            <p class="page-subtitle">게시글 #<%=idx%> · 작성일 <%=board.getWritedate()%></p>
        </div>
        <div class="card-body">
            <form name="edit" action="<%=request.getContextPath()%>/board/modify.do" method="POST" class="row g-4">
                <input type="hidden" name="idx" value="<%=idx%>">
                <div class="col-md-6"><label class="form-label">작성자</label><input type="text" name="writer" class="form-control" value="<%=board.getWriter()%>"></div>
                <div class="col-md-6"><label class="form-label">비밀번호 확인</label><input type="password" name="pwd" class="form-control" placeholder="기존 비밀번호"></div>
                <div class="col-md-6"><label class="form-label">이메일</label><input type="email" name="email" class="form-control" value="<%=board.getEmail()%>"></div>
                <div class="col-md-6"><label class="form-label">홈페이지</label><input type="text" name="homepage" class="form-control" value="<%=board.getHomepage()%>"></div>
                <div class="col-12"><label class="form-label">제목</label><input type="text" name="subject" class="form-control" value="<%=board.getSubject()%>"></div>
                <div class="col-12"><label class="form-label">내용</label><textarea name="content" class="form-control"><%=board.getContent()%></textarea></div>
                <div class="col-12">
                    <label class="form-label">첨부 파일</label>
                    <div class="meta-pill mb-2">현재 파일: <strong><%=board.getFilename()%></strong> (<%=board.getFilesize()%> bytes)</div>
                    <input type="file" name="filename" class="form-control">
                </div>
                <div class="col-12 d-flex justify-content-end gap-2">
                    <a href="<%=request.getContextPath()%>/board/list.do" class="btn btn-outline-secondary rounded-pill px-4">목록</a>
                    <button type="reset" class="btn btn-light rounded-pill px-4">초기화</button>
                    <button type="button" class="btn btn-primary rounded-pill px-4" onclick="editCheck();">수정 저장</button>
                </div>
            </form>
        </div>
    </section>
</main>
<% pageContext.include("/WEB-INF/views/common/footer.jsp"); %>
</body>
</html>