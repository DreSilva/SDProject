<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Selecionar Eleição Editar</title>
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

<s:form action="editarEleicao" method="post">
    <s:fielderror name="tError"/>
    <s:text name="Tipo Eleição"/>
    <s:radio label="Lista" name="tipo" list="tipos"
             listKey="key" listValue="value"/><br>

    <s:text name="Titulo Eleição"/>
    <s:textfield label="Titulo" name="titulo" value="%{titulo}"/><br>

    <s:text name="Descrição Eleição"/>
    <s:textfield label="descricao" name="descricao"  value="%{descricao}" /><br>

    <s:text name="Data Inicial Eleição"/>
    <sx:datetimepicker name="dataInicial" displayFormat="dd-MM-yyyy" />
    <s:text name="Hora"/>
    <s:textfield type="time" name="horaI" value="%{horaI}"/><br>

    <s:text name="Data Final Eleição"/>
    <sx:datetimepicker name="dataFinal" displayFormat="dd-MM-yyyy"/>
    <s:text name="Hora"/>
    <s:textfield type="time" name="horaF" value="%{horaF}"/><br>
    <s:submit onclick=""/>
</s:form>
</body>
</html>
