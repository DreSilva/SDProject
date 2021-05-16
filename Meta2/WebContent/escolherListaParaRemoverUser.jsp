<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Escolher Lista</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="escolherLista" method="post">
            <s:fielderror name="tError"/>
            <s:radio label="Lista" name="lista" list="listas"
                     listKey="key" listValue="value"/><br>
            <s:submit/>
        </s:form>
    </c:otherwise>
</c:choose>



</body>
</html>
