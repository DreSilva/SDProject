<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolher Opção Para a Mesa</title>
</head>
<body>

<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="goAdicionarMesa" method="post">
            <button value="adicionarMesa" type="submit" name="adicionarMesa">Adicionar Mesa Eleição</button>
        </s:form>

        <s:form action="goRemoverMesa" method="post">
            <button value="removerMesa" type="submit" name="removerMesa">Remover Mesa Eleição</button>
        </s:form>
    </c:otherwise>
</c:choose>




</body>
</html>
