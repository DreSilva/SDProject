<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Criar Lista</title>
</head>
<body>

<s:form action="criarLista" method="post">
    <s:fielderror name="tError"/>
    <s:text name="Nome" />
    <s:textfield  name="nome"/><br>
    <s:radio label="Tipo Eleicao" name="tipo" list="tipos"
             listKey="key" listValue="value" value="Estudante" /><br>
    <s:submit onclick=""/>
</s:form>

</body>
</html>
