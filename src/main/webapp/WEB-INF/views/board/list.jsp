<%@page import="kr.or.bit.utils.PageUtil"%>
<%@page import="kr.or.bit.dto.BoardDTO"%>
<%@page import="java.util.List"%>
<%@page import="kr.or.bit.service.BoardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%
    BoardService service = BoardService.getInBoardService();
    int totalboardcount = service.totalBoardCount();

    String ps = request.getParameter("ps");
    String cp = request.getParameter("cp");
    if (ps == null || ps.trim().equals("")) ps = "5";
    if (cp == null || cp.trim().equals("")) cp = "1";

    int pagesize = Integer.parseInt(ps);
    int cpage = Integer.parseInt(cp);
    int pagecount = (totalboardcount / pagesize) + ((totalboardcount % pagesize) > 0 ? 1 : 0);
    List<BoardDTO> list = service.list(cpage, pagesize);
%>
<c:set var="pagesize" value="<%=pagesize%>" />
<c:set var="cpage" value="<%=cpage%>" />
<c:set var="pagecount" value="<%=pagecount%>" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 목록</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/board.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />
<main class="container board-shell">
    <section class="card board-card">
        <div class="card-header d-flex flex-column flex-lg-row justify-content-between gap-3">
            <div>
                <h2 class="page-title">게시글 목록</h2>
                <p class="page-subtitle">전체 <strong><%= totalboardcount %></strong>개의 글을 답글 순서대로 보여줍니다.</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <form name="list" class="d-flex align-items-center gap-2">
                    <label class="text-muted small fw-bold" for="ps">표시</label>
                    <select id="ps" name="ps" class="form-select form-select-sm" onchange="submit()">
                        <c:forEach var="i" begin="5" end="20" step="5">
                            <option value="${i}" <c:if test="${pagesize == i}">selected</c:if>>${i}건</option>
                        </c:forEach>
                    </select>
                </form>
                <a class="btn btn-primary rounded-pill px-4" href="<%=request.getContextPath()%>/board/writeForm.do">글쓰기</a>
            </div>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-board align-middle">
                    <thead>
                    <tr>
                        <th class="text-center">번호</th>
                        <th>제목</th>
                        <th class="text-center">작성자</th>
                        <th class="text-center">작성일</th>
                        <th class="text-center">조회</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="<%= list == null || list.size() == 0 %>">
                            <tr><td colspan="5" class="text-center text-muted py-5">등록된 게시글이 없습니다.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="board" items="<%=list%>">
                                <tr>
                                    <td class="text-center fw-bold text-muted">${board.idx}</td>
                                    <td>
                                        <span class="reply-depth" style="--depth:${board.depth}"></span>
                                        <c:if test="${board.depth > 0}"><span class="reply-badge">↳ 답글</span></c:if>
                                        <a class="title-link ms-1" href="<%=request.getContextPath()%>/board/detail.do?idx=${board.idx}&cp=${cpage}&ps=${pagesize}">
                                            <c:choose>
                                                <c:when test="${board.subject != null && fn:length(board.subject) > 28}">${fn:substring(board.subject,0,28)}...</c:when>
                                                <c:otherwise>${board.subject}</c:otherwise>
                                            </c:choose>
                                        </a>
                                    </td>
                                    <td class="text-center">${board.writer}</td>
                                    <td class="text-center text-muted">${board.writedate}</td>
                                    <td class="text-center"><span class="badge rounded-pill text-bg-light">${board.readnum}</span></td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <div class="d-flex flex-column flex-md-row justify-content-between align-items-center gap-3 mt-4">
                <nav aria-label="게시글 페이지">
                    <ul class="pagination mb-0">
                        <li class="page-item <c:if test='${cpage <= 1}'>disabled</c:if>">
                            <a class="page-link" href="?cp=${cpage-1}&ps=${pagesize}">이전</a>
                        </li>
                        <c:forEach var="i" begin="1" end="${pagecount}" step="1">
                            <li class="page-item <c:if test='${cpage == i}'>active</c:if>"><a class="page-link" href="?cp=${i}&ps=${pagesize}">${i}</a></li>
                        </c:forEach>
                        <li class="page-item <c:if test='${cpage >= pagecount}'>disabled</c:if>">
                            <a class="page-link" href="?cp=${cpage+1}&ps=${pagesize}">다음</a>
                        </li>
                    </ul>
                </nav>
                <span class="meta-pill">현재 페이지 <strong><%= cpage %></strong> / <%= Math.max(pagecount, 1) %></span>
            </div>
        </div>
    </section>
</main>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>