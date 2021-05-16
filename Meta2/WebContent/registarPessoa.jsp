<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Registar Pessoas</title>
    <sx:head />
    <s:head />
</head>
<body>

<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>

        <s:form action="registarPessoa" method="post">
            <s:fielderror name="tError"/>
            <s:text name="Usename:" />
            <s:textfield  name="user"/><br>
            <s:text name="Password:" />
            <s:textfield type="password" name="password" /><br>
            <s:radio label="Tipo" name="tipo" list="tipos"
                     listKey="key" listValue="value" value="Estudante" /><br>
            <s:radio label="Departamento" name="departamento" list="departamentos"
                     listKey="key" listValue="value" value="DEi" /><br>
            <s:text name="Contacto:" />
            <s:textfield name="contacto" /><br>
            <s:text name="Morada:" />
            <s:textfield name="morada" /><br>
            <s:text name="CC:" />
            <s:textfield name="CC" /><br>
            <s:text name="Data Validade: "/>
            <input type="date" name="dataValidade">
            <s:submit/>
        </s:form>
    </c:otherwise>
</c:choose>



</body>
</html>
