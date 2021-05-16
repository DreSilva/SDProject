<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Escolher Eleicao</title>
</head>
<body>

<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="escolherListaGerir" method="post">
            <s:text name="Eleição a Gerir Mesas"/>
            <s:radio label="Eleição" name="eleicao" list="eleicoes"
                     listKey="key" listValue="value" value="Estudante" /><br>
            <s:submit onclick=""/>
        </s:form>
    </c:otherwise>
</c:choose>


</body>
</html>
