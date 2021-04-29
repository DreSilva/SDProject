<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Registar Pessoas</title>
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
    <sx:datetimepicker name="dataValidade" displayFormat="dd-MM-yyyy"/><br>
    <s:submit/>
</s:form>

</body>
</html>
