<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Escolher Opção Para a Mesa</title>
</head>
<body>

<s:form action="goAdicionarMesa" method="post">
    <button value="adicionarMesa" type="submit" name="adicionarMesa">Adicionar Mesa Eleição</button>
</s:form>

<s:form action="goRemoverMesa" method="post">
    <button value="removerMesa" type="submit" name="removerMesa">Remover Mesa Eleição</button>
</s:form>

</body>
</html>
