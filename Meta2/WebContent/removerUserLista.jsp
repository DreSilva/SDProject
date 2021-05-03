<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolher Users</title>
</head>
<body>
<s:form action="removerUser" method="post">
    <s:fielderror name="tError"/>
    <s:checkboxlist name="user" list="users" listKey="key" listValue="value" label="Utilizadores"/>
    <s:submit />
</s:form>


</body>
</html>
