<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유게시판 글쓰기</title>
</head>
<body>
	<%
		String sessionId = (String)session.getAttribute("sessionId");
		if(sessionId == null) {
	%>
		<script type="text/javascript">
			alert('로그인 한 회원만 글쓰기가 가능합니다.');
			window.location.href='login';
		</script>
		
	<%		
		} 
	%>	
	
	<h2>자유게시판 글쓰기</h2>
	<hr>
	<form action="write">
		<input type="hidden" value="${memberDto.mid }" name="mid">
		<input type="hidden" value="${memberDto.mname }" name="mname"> 
		아이디 : ${memberDto.mid }<br><br>
		이 름 : ${memberDto.mname }<br><br>
		제 목 : <input type="text" name="ftitle" size="60"><br><br>
		내 용 : <textarea rows="10" cols="45" name="fcontent"></textarea><br><br>
		<input type="submit" value="입력">
		<input type="button" value="목록" onclick="script:window.location.href='list'">
	
	</form>
</body>
</html>