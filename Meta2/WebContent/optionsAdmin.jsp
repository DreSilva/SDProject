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

<s:form action="goEditarEleicao" method="post">
    <button value="editarEleicao" type="submit" name="editarEleicao">Editar Eleição</button>
</s:form>

<s:form action="goGerirMesasVoto" method="post">
    <button value="gerirMesas" type="submit" name="gerirMesas">Gerir Mesas de Voto</button>
</s:form>

<s:form action="goObterInformacaoPassada" method="post">
    <button value="gerirMesas" type="submit" name="gerirMesas">Informação Eleição Passada</button>
</s:form>

</body>
</html>
