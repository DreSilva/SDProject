<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolher Eleicao</title>
</head>
<body>

<s:form action="escolherListaGerir" method="post">
    <s:text name="Eleição a Gerir Mesas"/>
    <s:radio label="Eleição" name="eleicao" list="eleicoes"
             listKey="key" listValue="value" value="Estudante" /><br>
    <s:submit onclick=""/>
</s:form>


</body>
</html>
