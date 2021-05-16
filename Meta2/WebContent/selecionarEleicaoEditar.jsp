<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Selecionar Eleição Editar</title>
</head>
<body>


<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="selecionarEleicaoEditar" method="post">
            <s:fielderror name="tError"/>
            <s:radio label="Lista" name="eleicao" list="eleicoes"
                     listKey="key" listValue="value"/><br>
            <s:submit onclick=""/>
        </s:form>
    </c:otherwise>
</c:choose>



</body>
</html>
