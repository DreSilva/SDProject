<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Adicionar Mesa Eleicao</title>
</head>
<body>

<s:form action="removeMesaEleicao" method="post">
    <s:fielderror name="tError"/>
    <s:text name="Escolha Mesas a Remover"/>
    <s:checkboxlist name="mesa" list="mesas" listKey="key" listValue="value" />
    <s:submit />
</s:form>

</body>
</html>
