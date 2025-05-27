<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 실패</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
<link href="../../resources/css/user/create_result.css" rel="stylesheet" type="text./css">

</head>
<body>
	<%-- <%@ include file ="../include/header2.jsp" %>
	<%@ include file ="../include/loginHeader.jsp" %> --%>
	<%-- <%@ include file="../include/nav.jsp" %> --%>
	<%-- <%@ include file="../include/loginNav.jsp" %> --%>
	<%-- <%@ include file ="../include/new_header.jsp" %> --%>
	<section>
		<div id="section_wrap">
			<div class="word">
			<h3>회원가입에 실패했습니다.</h3>
				<p><%= request.getAttribute("error_message") %></p>
			</div>
			<div class="other">
				<a href="/user/create">회원가입</a>
			</div>
		</div>
	</section>
</body>
</html>