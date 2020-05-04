<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach varStatus="loop" var="compra" items="${requestScope.fechaMasCompra}">
    <tr>
        <th scope="row">${loop.count}</th>
        <td><fmt:formatDate value="${compra.fecha.time}" type="date" dateStyle="short" /></td>
        <td>${compra.cantidad}</td>
    </tr>
</c:forEach>