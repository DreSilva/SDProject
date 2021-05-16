<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Informacao Velha</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <c:forEach items="${infos}" var="value">
            <c:out value="${value}" /><br>
        </c:forEach>
    </c:otherwise>
</c:choose>


</body>
</html>
