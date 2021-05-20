<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Escolher Eleicao Passada</title>
</head>
<body>

<c:forEach items="${eleicoes}" var="value" varStatus="status">
    <s:form action="escolherEleicaoPassada/%{#attr.status.count}" method="post">
        <button value="notficications" type="submit" >${value.value}</button>
    </s:form>
</c:forEach>


</body>
</html>
