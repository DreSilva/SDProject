<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Informacao Velha</title>
</head>
<body>
<c:forEach items="${infos}" var="value">
    <c:out value="${value}" /><br>
</c:forEach>



</body>
</html>
