<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach var="subs" items="${requestScope.productos}">
    <c:if test="${subs.porcentajeOferta != 0}">
        <tr>
            <td>${subs.nombre}</td>
            <td>${subs.precio}</td>
            <td>${subs.porcentajeOferta}</td>
            <td class="d-none d-sm-block">${subs.precioReal}</td>
            <td><button type="submit" class="btn btn-outline-danger" name="del">Eliminar</button></td>
        </tr>
    </c:if>
</c:forEach>