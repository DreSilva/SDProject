<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Selecionar Eleição Editar</title>
</head>
<body>

<c:choose>
    <c:when test="${HeyBean.adminLogIn==0}">
        <p>Precisa de fazer login para poder aceder!</p>
    </c:when>
    <c:otherwise>

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
            <input type="date" name="dataInicial" id="dI">
            <s:text name="Hora"/>
            <s:textfield type="time" name="horaI" value="%{horaI}"/><br>

            <s:text name="Data Final Eleição"/>
            <input type="date" name="dataFinal" id="dF">
            <s:text name="Hora"/>
            <s:textfield type="time" name="horaF" value="%{horaF}"/><br>
            <s:submit onclick=""/>

            <script type="text/javascript">
                var dateF = "${dataFinal}".split(" ");
                var day = dateF[2];
                var month1 = dateF[1].toLowerCase();
                var months = ["jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"];
                month1 = months.indexOf(month1);
                if(month1.toString().length){
                    month1="0"+month1.toString();
                }
                var year = dateF[5];
                var finalDateString=year+"-"+month1+"-"+day;
                document.getElementById("dF").value=finalDateString;


                var dateI = "${dataInicial}".split(" ");
                var day2 = dateI[2];
                var month2 = dateI[1].toLowerCase();
                month2 = months.indexOf(month2);
                if(month2.toString().length){
                    month2="0"+month2.toString();
                }
                var year2 = dateI[5];
                var finalDateString2=year2+"-"+month2+"-"+day2;
                document.getElementById("dI").value=finalDateString2;
            </script>
        </s:form>
    </c:otherwise>
</c:choose>



</body>
</html>
