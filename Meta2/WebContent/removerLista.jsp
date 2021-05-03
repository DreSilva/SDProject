<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Remover Lista</title>
</head>
<body>

<s:form action="removerLista" method="post">
    <s:fielderror name="tError"/>
    <s:radio label="Lista" name="lista" list="listas"
             listKey="key" listValue="value"/><br>
    <s:submit onclick=""/>
</s:form>
</body>
</html>
