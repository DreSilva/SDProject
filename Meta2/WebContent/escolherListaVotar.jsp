<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="doVoto" method="post">
            <s:fielderror name="tError"/>
            <s:text name="Lista Para Votar"/>
            <s:radio label="Lista" name="lista" list="listas"
                     listKey="key" listValue="value" /><br>
            <s:submit onclick=""/>
        </s:form>
    </c:otherwise>
</c:choose>

</body>
</html>
