<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolhe Opção</title>
</head>
<body>

<s:form action="goRegistarPessoa" method="post">
    <button value="registarPessoa" type="submit" name="registarPesssoa">Registar Pessoa</button>
</s:form>

<s:form action="goCriarEleicao" method="post">
    <button value="criarEleicao" type="submit" name="criarEleicao">Criar Eleicao</button>
</s:form>

<s:form action="goGerirLista" method="post">
    <button value="gerirLista" type="submit" name="gerirLista">Gerir Lista</button>
</s:form>

</body>
</html>
