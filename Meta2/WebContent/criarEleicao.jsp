<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>LogIn</title>
    <sx:head />
    <s:head />
</head>
<body>

<struts>  <!-- POR ISTO SEMPRE QUE SE QUER USAR DATEPICKER -->
    <constant name="struts.ui.templateDir" value="templates" />
    <constant name="struts.ui.theme" value="simple" />
    <constant name="struts.xwork.chaining.copyErrors" value="true"/>
    <constant name="struts.xwork.chaining.copyFieldErrors" value="true"/>
    <constant name="struts.xwork.chaining.copyErrors" value="true"/>
</struts>

<s:form action="criarEleicao" method="post">
    <s:fielderror name="tError"/>
    <s:text name="Titulo:" />
    <s:textfield  name="titulo"/><br>
    <s:text name="Descrição:" />
    <s:textfield name="descricao" /><br>
    <s:text name="Data Inicial: "/>
    <sx:datetimepicker name="dataInicial" id="inicio" displayFormat="dd-MM-yyyy"/>
    <s:text name="Hora"/>
    <s:textfield type="time" name="hourI"/><br>
    <s:text name="Data Final: "/>
    <sx:datetimepicker name="dataFinal" id="final" displayFormat="dd-MM-yyyy"/>
    <s:text name="Hora"/>
    <s:textfield type="time" name="hourF"/><br>
    <s:radio label="Tipo Eleicao" name="tipo" list="tipos"
             listKey="key" listValue="value" value="Estudante" /><br>

    <s:submit onclick=""/>
</s:form>
</body>
</html>