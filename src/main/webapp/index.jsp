<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="Stylesheet" href="assets/css/board.css">
</head>
<body>
	<%
		pageContext.include("/WEB-INF/views/common/header.jsp");
	%>
	<div id="pageContainer">
		<h3>UI(Css 怨듯넻 ?섏씠吏 ?곸슜 ?뺤씤)</h3>
		<h3>DB?곌껐 ?뺣낫 ?뺤씤</h3>
		<%
		Connection conn = null;

		Context context = new InitialContext(); //?꾩옱 ?꾨줈?앺듃???대쫫湲곕컲 寃??
		DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/oracle");//java:comp/env/ + name

		//POOL?덉뿉??connection 媛吏怨??ㅺ린
		conn = ds.getConnection();
		
		out.print("db ?곌껐?щ? : " + conn.isClosed() + "<br>");
		
		//POINT
		//POOL??connection 諛쏇솚?섍린
		conn.close(); //諛섑솚?섍린
		
		out.print("db ?곌껐?щ? : " + conn.isClosed() + "<br>");
		%>
	</div>
</body>
</html>
