<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach varStatus="loop" var="compra" items="${requestScope.masComprados}">
    <tr>
        <th scope="row">${loop.count}</th>
        <td id="maxCompNom${loop.count}"><c:out value="${compra.nombre}"/></td>
        <td id="maxCompCant${loop.count}"><c:out value="${compra.cantidad}"/></td>
    </tr>
</c:forEach>