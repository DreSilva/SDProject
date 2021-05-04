<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Adicionar Lista a Eleicao</title>
</head>
<body>
<s:form action="adicionarListaEleicao" method="post">
    <s:fielderror name="tError"/>
    <s:text name="Lista a Adicionar"/>
    <s:radio label="Lista" name="lista" list="listas"
             listKey="key" listValue="value"/><br>
    <s:text name="Eleição a Adicionar Lista"/>
    <s:radio label="Eleição" name="eleicao" list="eleicoes"
             listKey="key" listValue="value" value="Estudante" /><br>

    <s:submit onclick=""/>
</s:form>
</body>
</html>