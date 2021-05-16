<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>LogIn</title>
    <s:head />
</head>
<body>
<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>
        <s:form action="criarEleicao" method="post">
            <s:fielderror name="tError"/>
            <s:text name="Titulo:" />
            <s:textfield  name="titulo"/><br>
            <s:text name="Descrição:" />
            <s:textfield name="descricao" /><br>
            <s:text name="Data Inicial: "/>
            <input type="date" name="dataInicial" id="di">
            <s:text name="Hora"/>
            <s:textfield type="time" name="hourI"/><br>
            <s:text name="Data Final: "/>
            <input type="date" name="dataFinal" id="df">


            <s:text name="Hora"/>
            <s:textfield type="time" name="hourF"/><br>
            <s:radio label="Tipo Eleicao" name="tipo" list="tipos"
                     listKey="key" listValue="value" value="Estudante" /><br>

            <s:submit onclick=""/>
        </s:form>
    </c:otherwise>
</c:choose>

</body>
</html>