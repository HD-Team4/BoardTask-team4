<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TEAM4 Board</title>
    <link rel="stylesheet" href="assets/css/board.css">
</head>
<body>
    <%
        pageContext.include("/WEB-INF/views/common/header.jsp");
    %>
    <div id="pageContainer" class="container board-shell">
        <div class="board-card card">
            <div class="card-body">
                <h3 class="page-title">UI CSS 공통 페이지 적용 확인</h3>
                <p class="page-subtitle">공통 헤더와 board.css가 정상 적용되는지 확인합니다.</p>

                <hr>

                <h3 class="page-title fs-4">DB 연결 정보 확인</h3>
                <%
                Connection conn = null;

                Context context = new InitialContext();
                DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/oracle");

                // 커넥션 풀에서 connection 가져오기
                conn = ds.getConnection();
                out.print("<p class=\"meta-pill mt-3\">DB 연결 종료 여부 : " + conn.isClosed() + "</p>");

                // 커넥션 풀에 connection 반환하기
                conn.close();
                out.print("<p class=\"meta-pill\">DB 연결 종료 여부 : " + conn.isClosed() + "</p>");
                %>
            </div>
        </div>
    </div>
    <%
        pageContext.include("/WEB-INF/views/common/footer.jsp");
    %>
</body>
</html>
