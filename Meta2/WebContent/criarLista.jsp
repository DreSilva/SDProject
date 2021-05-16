<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Criar Lista</title>
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="criarLista" method="post">
            <s:fielderror name="tError"/>
            <s:text name="Nome" />
            <s:textfield  name="nome"/><br>
            <s:radio label="Tipo Eleicao" name="tipo" list="tipos"
                     listKey="key" listValue="value" value="Estudante" /><br>
            <s:submit onclick=""/>
        </s:form>
    </c:otherwise>
</c:choose>



</body>
</html>
