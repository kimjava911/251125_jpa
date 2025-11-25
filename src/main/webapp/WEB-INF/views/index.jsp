<%@ page import="kr.java.jpa.model.entity.Member" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>메인 페이지</title>
</head>
<body>
    <p>메인 페이지</p>
    <h1>회원목록</h1>
    <%
        List<Member> members = (List<Member>) request.getAttribute("members");
        for (Member m : members) {
    %>
        <div>
            <p>유저번호 : <%= m.getMemberId() %></p>
            <p>유저네임 : <%= m.getUsername() %></p>
            <p>생성일시 : <%= m.getCreatedAt() %></p>
        </div>
    <% } %>

    <h1>회원가입</h1>
    <form method="post">
        <input name="username" placeholder="username 입력"><br>
        <input name="password" type="password" placeholder="password 입력"><br>
        <button>가입</button>
    </form>
</body>
</html>
