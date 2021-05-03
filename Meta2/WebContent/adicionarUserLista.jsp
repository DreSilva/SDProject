<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Adicionar User Lista</title>
</head>
<body>

<s:form action="adicionarUser" method="post">
    <s:fielderror name="tError"/>
    <s:radio label="Eleição" name="lista" list="listas"
             listKey="key" listValue="value"/><br>
    <s:checkboxlist name="user" list="users" listKey="key" listValue="value" label="Utilizadores"/>
    <s:submit />
</s:form>

</body>
</html>
