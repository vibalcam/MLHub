<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach var="subs" items="${requestScope.productos}">
    <c:if test="${subs.porcentajeOferta != 0}">
        <tr>
            <td><c:out value="${subs.nombre}"/></td>
            <td><c:out value="${subs.precio}"/></td>
            <td><c:out value="${subs.porcentajeOferta}"/></td>
            <td class="d-none d-sm-block"><c:out value="${subs.precioReal}"/></td>
            <td><button type="button" class="btn btn-outline-danger deleteOferta" value="<c:out value="${subs.id}"/>">Eliminar</button></td>
        </tr>
    </c:if>
</c:forEach>