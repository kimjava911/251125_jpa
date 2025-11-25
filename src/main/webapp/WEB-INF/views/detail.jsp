<%@ page import="kr.java.jpa.model.entity.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>개별 페이지</title>
</head>
<body>
    <p>개별 페이지</p>

    <%
        Member m = (Member) request.getAttribute("member");
    %>

    <form method="post" action="<%= request.getContextPath() %>/<%= m.getMemberId() %>/password">
        <input name="password" type="password" placeholder="password 입력"><br>
        <button>새로운 비밀번호로 변경</button>
    </form>

    <form method="post">
        <input name="username" placeholder="username 입력"
               value="<%= m.getUsername() %>"><br>
        <input name="password" type="password" placeholder="password 입력"
               value="<%= m.getPassword() %>"><br>
        <button>변경</button>
    </form>

    <form method="post" action="<%= request.getContextPath() %>/<%= m.getMemberId() %>/delete">
        <button>삭제</button>
    </form>

    <div>
        <p>유저번호 : <%= m.getMemberId() %></p>
        <p>유저네임 : <%= m.getUsername() %></p>
        <p>생성일시 : <%= m.getCreatedAt() %></p>
    </div>
    <a href="<%= request.getContextPath() %>/">메인 페이지로</a>
</body>
</html>
