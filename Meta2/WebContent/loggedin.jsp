<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Menu</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.userLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <p><a href="<s:url action="vote"/>">Votar</a></p>
        <p><a href="<s:property value="authorizationUrl"/>">Associar conta do Facebook</a></p>
        <p><a href="<s:url action="shareEsta" />">Share statistics of finished Elections</a></p>
    </c:otherwise>
</c:choose>

</body>
</html>