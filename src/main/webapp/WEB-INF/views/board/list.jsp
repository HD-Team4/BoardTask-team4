<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시글 목록</title>
    <!-- Bootstrap CSS CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/board.css">
</head>
<body>
<c:import url="/WEB-INF/views/common/header.jsp" />
<main class="container board-shell">
    <section class="card board-card">
        <div class="card-header d-flex flex-column flex-lg-row justify-content-between gap-3">
            <div>
                <h2 class="page-title">게시글 목록</h2>
                <p class="page-subtitle">전체 <strong>${totalboardcount}</strong>개의 글을 답글 순서대로 보여줍니다.</p>
            </div>
            <div class="d-flex align-items-center gap-2">
                <form name="listForm" action="${pageContext.request.contextPath}/board/list.do" method="get" class="d-flex align-items-center gap-2">
                    <input type="hidden" name="cp" value="${cpage}">
                    <label class="text-muted small fw-bold" for="ps">표시</label>
                    <select id="ps" name="ps" class="form-select form-select-sm" onchange="submit()">
                        <c:forEach var="i" begin="5" end="20" step="5">
                            <option value="${i}" <c:if test="${pagesize == i}">selected</c:if>>${i}건</option>
                        </c:forEach>
                    </select>
                </form>
                <a class="btn btn-primary rounded-pill px-4" href="${pageContext.request.contextPath}/board/writeForm.do">글쓰기</a>
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
                        <c:when test="${empty list}">
                            <tr><td colspan="5" class="text-center text-muted py-5">등록된 게시글이 없습니다.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="board" items="${list}">
                                <tr>
                                    <td class="text-center fw-bold text-muted">${board.boardId}</td>
                                    <td>
                                        <span class="reply-depth" style="--depth:${board.reLevel}"></span>
                                        <c:if test="${board.reLevel > 0}"><span class="reply-badge">↳ 답글</span></c:if>
                                        <a class="title-link ms-1" href="${pageContext.request.contextPath}/board/detail.do?boardId=${board.boardId}&cp=${cpage}&ps=${pagesize}">
                                            <c:choose>
                                                <c:when test="${board.title != null && fn:length(board.title) > 28}">
                                                    <c:out value="${fn:substring(board.title, 0, 28)}..."/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${board.title}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </a>
                                    </td>
                                    <td class="text-center"><c:out value="${board.writer}"/></td>
                                    <td class="text-center text-muted">${board.createdAt}</td>
                                    <td class="text-center"><span class="badge rounded-pill text-bg-light">${board.readCount}</span></td>
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
                            <li class="page-item <c:if test='${cpage == i}'>active</c:if>">
                                <a class="page-link" href="?cp=${i}&ps=${pagesize}">${i}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item <c:if test='${cpage >= pagecount}'>disabled</c:if>">
                            <a class="page-link" href="?cp=${cpage+1}&ps=${pagesize}">다음</a>
                        </li>
                    </ul>
                </nav>
                <span class="meta-pill">현재 페이지 <strong>${cpage}</strong> / <c:out value="${pagecount > 0 ? pagecount : 1}"/></span>
            </div>
        </div>
    </section>
</main>
<c:import url="/WEB-INF/views/common/footer.jsp" />
</body>
</html>