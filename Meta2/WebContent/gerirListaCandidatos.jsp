<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gerir Lista de Candidatos</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="goAdicionarListaEleicao" method="post">
            <button value="adicionarListaEleicao" type="submit" name="adicionarListaEleicao">Adicionar Lista a Eleição</button>
        </s:form>

        <s:form action="goRemoverListaEleicao" method="post">
            <button value="removerListaEleicao" type="submit" name="removerListaEleicao">Remover Lista a Eleição</button>
        </s:form>

        <s:form action="goCriarLista" method="post">
            <button value="criarLista" type="submit" name="criarLista">Criar Lista</button>
        </s:form>

        <s:form action="goRemoverLista" method="post">
            <button value="removerLista" type="submit" name="removerLista">Remover Lista</button>
        </s:form>

        <s:form action="goAddUserLista" method="post">
            <button value="addUserLista" type="submit" name="addUserLista">Adicionar Utilizador Lista</button>
        </s:form>

        <s:form action="goRemoverUserLista" method="post">
            <button value="removeUserLista" type="submit" name="removeUserLista">Remove Utilizador Lista</button>
        </s:form>
    </c:otherwise>
</c:choose>


</body>
</html>
