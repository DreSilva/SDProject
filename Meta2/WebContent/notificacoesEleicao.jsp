<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolher Eleicao Notificar</title>
</head>
<body>


<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <c:forEach items="${eleicoes}" var="value" varStatus="status">
            <s:form action="notifications/%{#attr.status.count}" method="post">
                <button value="notficications" type="submit" >${value.value}</button>
            </s:form>
        </c:forEach>
    </c:otherwise>
</c:choose>

</body>
</html>
