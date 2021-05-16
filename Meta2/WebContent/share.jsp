<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Vote</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.userLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="shareelection" method="post">
            <s:fielderror name="tError"/>
            <s:text name="Eleição que pretende partilhar"/>
            <s:radio label="Eleição" name="eleicao" list="eleicoes"
                     listKey="key" listValue="value" value="Estudante" /><br>
            <s:submit onclick=""/>
        </s:form>
    </c:otherwise>
</c:choose>



</body>
</html>
