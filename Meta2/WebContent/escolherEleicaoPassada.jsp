<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Escolher Eleicao Passada</title>
</head>
<body>
<s:form action="escolherEleicaoPassada" method="post">
    <s:text name="Eleição Passada"/>
    <s:radio name="eleicao" list="eleicoes"
             listKey="key" listValue="value"/><br>
    <s:submit onclick=""/>
</s:form>


</body>
</html>
