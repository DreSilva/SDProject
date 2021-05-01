<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Adicionar Lista a Eleicao</title>
</head>
<body>
<s:form action="criarEleicao" method="post">
    <s:fielderror name="tError"/>
    <s:radio label="Tipo Eleicao" name="tipo" list="tipos"
             listKey="key" listValue="value" value="Estudante" /><br>
    <s:radio label="Lista" name="lista" list="listas"
             listKey="key" listValue="value"/><br>
    <s:radio label="Eleição" name="eleiao" list="eleicoes"
             listKey="key" listValue="value" value="Estudante" /><br>

    <s:submit onclick=""/>
</s:form>
</body>
</html>