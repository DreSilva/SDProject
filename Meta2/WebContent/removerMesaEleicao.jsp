<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Adicionar Mesa Eleicao</title>
</head>
<body>


<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="removeMesaEleicao" method="post">
            <s:fielderror name="tError"/>
            <s:text name="Escolha Mesas a Remover"/>
            <s:checkboxlist name="mesa" list="mesas" listKey="key" listValue="value" />
            <s:submit />
        </s:form>
    </c:otherwise>
</c:choose>


</body>
</html>
