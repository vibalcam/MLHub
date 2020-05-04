<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach varStatus="loop" var="compra" items="${requestScope.masComprados}">
    <tr>
        <th scope="row">${loop.count}</th>
        <td>${compra.nombre}</td>
        <td>${compra.cantidad}</td>
    </tr>
</c:forEach>