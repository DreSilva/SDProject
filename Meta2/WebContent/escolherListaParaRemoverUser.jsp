<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolher Lista</title>
</head>
<body>

<s:form action="escolherLista" method="post">
    <s:fielderror name="tError"/>
    <s:radio label="Lista" name="lista" list="listas"
             listKey="key" listValue="value"/><br>
    <s:submit/>
</s:form>

</body>
</html>
