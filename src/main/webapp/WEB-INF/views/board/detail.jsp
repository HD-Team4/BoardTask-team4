<%@page import="kr.or.bit.dto.CommentDTO"%>
<%@page import="java.util.List"%>
<%@page import="kr.or.bit.dto.BoardDTO"%>
<%@page import="kr.or.bit.service.BoardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String idx = request.getParameter("idx");
    if (idx == null || idx.trim().equals("")) {
        response.sendRedirect(request.getContextPath() + "/board/list.do");
        return;
    }
    String cpage = request.getParameter("cp");
    String pagesize = request.getParameter("ps");
    if (cpage == null || cpage.trim().equals("")) cpage = "1";
    if (pagesize == null || pagesize.trim().equals("")) pagesize = "5";

    BoardService service = BoardService.getInBoardService();
    service.addReadNum(idx);
    BoardDTO board = service.content(Integer.parseInt(idx));
    if (board == null) {
        out.print("데이터를 찾을 수 없습니다.");
        return;
    }
    List<CommentDTO> replylist = service.replyList(idx);
    String content = board.getContent();
    if (content != null) content = content.replace("\n", "<br>");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 상세</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/comment.css">
    <script>
        function reply_check() {
            var frm = document.reply;
            if (frm.reply_writer.value === "" || frm.reply_content.value === "" || frm.reply_pwd.value === "") {
                alert("댓글 작성자, 내용, 비밀번호를 모두 입력하세요.");
                return false;
            }
            frm.submit();
        }
        function reply_del(frm) {
            if (frm.delPwd.value === "") {
                alert("비밀번호를 입력하세요.");
                frm.delPwd.focus();
                return false;
            }
            frm.submit();
        }
    </script>
</head>
<body>
<% pageContext.include("/WEB-INF/views/common/header.jsp"); %>
<main class="container board-shell">
    <section class="card board-card mb-4">
        <div class="card-header">
            <div class="d-flex flex-column flex-lg-row justify-content-between gap-3">
                <div>
                    <h2 class="page-title"><%=board.getSubject()%></h2>
                    <p class="page-subtitle">#<%=idx%> · <%=board.getWriter()%> · <%=board.getWritedate()%></p>
                </div>
                <div class="d-flex flex-wrap gap-2">
                    <span class="meta-pill">조회 <strong><%=board.getReadnum()%></strong></span>
                    <span class="meta-pill">파일 <strong><%=board.getFilename()%></strong></span>
                </div>
            </div>
        </div>
        <div class="card-body">
            <div class="detail-content"><%=content%></div>
            <div class="d-flex flex-wrap justify-content-end gap-2 mt-4">
                <a class="btn btn-outline-secondary rounded-pill px-4" href="<%=request.getContextPath()%>/board/list.do?cp=<%=cpage%>&ps=<%=pagesize%>">목록</a>
                <a class="btn btn-outline-primary rounded-pill px-4" href="<%=request.getContextPath()%>/board/modifyForm.do?idx=<%=idx%>&cp=<%=cpage%>&ps=<%=pagesize%>">수정</a>
                <form action="<%=request.getContextPath()%>/board/delete.do" method="POST" class="d-inline-flex gap-2">
                    <input type="hidden" name="idx" value="<%=idx%>">
                    <input type="password" name="pwd" class="form-control form-control-sm" placeholder="비밀번호">
                    <button type="submit" class="btn btn-outline-danger rounded-pill px-4">삭제</button>
                </form>
                <a class="btn btn-success rounded-pill px-4" href="<%=request.getContextPath()%>/board/replyForm.do?idx=<%=idx%>&cp=<%=cpage%>&ps=<%=pagesize%>&subject=<%=board.getSubject()%>">답글</a>
            </div>
        </div>
    </section>

    <section class="card board-card">
        <div class="card-header">
            <h3 class="page-title fs-4">댓글</h3>
            <p class="page-subtitle">게시글에 종속된 댓글 영역입니다.</p>
        </div>
        <div class="card-body">
            <form name="reply" action="<%=request.getContextPath()%>/comment/write.ajax" method="POST" class="comment-box row g-3 mb-4">
                <input type="hidden" name="idx" value="<%=idx%>">
                <input type="hidden" name="userid" value="">
                <div class="col-md-3"><input type="text" name="reply_writer" class="form-control" placeholder="작성자"></div>
                <div class="col-md-6"><input type="text" name="reply_content" class="form-control" placeholder="댓글 내용"></div>
                <div class="col-md-2"><input type="password" name="reply_pwd" class="form-control" placeholder="비밀번호"></div>
                <div class="col-md-1 d-grid"><button type="button" class="btn btn-primary" onclick="reply_check()">등록</button></div>
            </form>

            <div class="comment-list">
                <%
                    if (replylist != null && replylist.size() > 0) {
                        for (CommentDTO reply : replylist) {
                %>
                <div class="comment-item d-flex flex-column flex-md-row justify-content-between gap-3">
                    <div>
                        <strong><%=reply.getWriter()%></strong>
                        <time class="ms-2"><%=reply.getWritedate()%></time>
                        <p class="mb-0 mt-2"><%=reply.getContent()%></p>
                    </div>
                    <form action="<%=request.getContextPath()%>/comment/delete.ajax" method="POST" name="replyDel" class="d-flex gap-2 align-items-start">
                        <input type="hidden" name="no" value="<%=reply.getNo()%>">
                        <input type="hidden" name="idx" value="<%=idx%>">
                        <input type="password" name="delPwd" class="form-control form-control-sm" placeholder="비밀번호">
                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="reply_del(this.form)">삭제</button>
                    </form>
                </div>
                <%      }
                    } else {
                %>
                <div class="text-center text-muted py-4">아직 댓글이 없습니다.</div>
                <% } %>
            </div>
        </div>
    </section>
</main>
<% pageContext.include("/WEB-INF/views/common/footer.jsp"); %>
</body>
</html>