<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Selecionar Eleição Editar</title>
</head>
<body>

<s:form action="selecionarEleicaoEditar" method="post">
    <s:fielderror name="tError"/>
    <s:radio label="Lista" name="eleicao" list="eleicoes"
             listKey="key" listValue="value"/><br>
    <s:submit onclick=""/>
</s:form>
</body>
</html>
