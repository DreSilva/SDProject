<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>Escolher Eleicao</title>
</head>
<body>
<s:form action="escolherEleicaoParaRemover" method="post">
    <s:fielderror name="tError"/>
    <s:radio label="Eleição" name="eleicao" list="eleicoes"
             listKey="key" listValue="value"/><br>
    <s:submit onclick=""/>
</s:form>

</body>
</html>
