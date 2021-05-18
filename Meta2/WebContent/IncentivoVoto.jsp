<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Incentivar Voto?</title>
</head>
<body>
<p>Voto Registado com Sucesso! Pretende incentivar outras pessoas no facebook a fazer o mesmo?</p>
<c:choose>
    <c:when test="${HeyBean.FBid==null}">
        <p>Não há conta de Facebook associada esta conta.Associe se quiser partilhar</p>
        <p><a href="<s:url action="index" />">Voltar à página inicial</a></p>
    </c:when>
    <c:otherwise>
        <p><a href="<s:property value="authorizationUrl"/>">Share on Facebook</a></p>
        <p><a href="<s:url action="index" />">Não, obrigado</a></p>
    </c:otherwise>
</c:choose>

</body>
</html>
