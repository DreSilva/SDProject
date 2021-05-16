<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Adicionar User Lista</title>
</head>
<body>



<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="adicionarUser" method="post">
            <s:fielderror name="tError"/>
            <s:radio label="Eleição" name="lista" list="listas"
                     listKey="key" listValue="value"/><br>
            <s:checkboxlist name="user" list="users" listKey="key" listValue="value" label="Utilizadores"/>
            <s:submit />
        </s:form>
    </c:otherwise>
</c:choose>

</body>
</html>
