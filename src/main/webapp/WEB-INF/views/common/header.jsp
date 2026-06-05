<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="site-hero">
    <nav class="navbar navbar-expand-lg navbar-dark site-nav">
        <div class="container py-2">
            <a class="navbar-brand fw-bold" href="<%= request.getContextPath() %>/">
                TEAM4 Board
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav" aria-controls="mainNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="mainNav">
                <ul class="navbar-nav ms-auto gap-lg-2">
                    <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/board/list.do">게시글 목록</a></li>
                    <li class="nav-item"><a class="btn btn-light btn-sm rounded-pill px-3" href="<%= request.getContextPath() %>/board/writeForm.do">글쓰기</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container hero-copy">
        <span class="eyebrow">Servlet MVC Board</span>
        <h1>게시글, 댓글, 답글을 한 화면에서 관리합니다.</h1>
        <p>기존 JSP 흐름을 유지하면서 Bootstrap 기반 반응형 UI를 적용했습니다.</p>
    </div>
</header>
