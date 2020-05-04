<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach varStatus="loop" var="subs" items="${requestScope.productos}">
    <tr>
        <th scope="row"><input id="nivelProducto${subs.id}" type="number" min="0" class="form-control input-sm input-change" placeholder="Nivel" value="${subs.accessLevel.id}"></th>
        <td><input id="nombreProducto${subs.id}" type="text" class="form-control input-sm input-change" placeholder="Nombre" value="${subs.nombre}"></td>
        <td><input id="precioProducto${subs.id}" type="number" min="0" class="form-control input-sm input-change" placeholder="Precio" value="${subs.precio}"></td>
        <td>
            <button type="button" class="btn btn-outline-secondary mb-1 changeProducto" value="${subs.id}">Modificar</button>
            <button type="button" class="btn btn-outline-danger mb-1 deleteProducto" value="${subs.id}">Eliminar</button>
        </td>
    </tr>
</c:forEach>
<tr>
    <th scope="row"><input id="nivelAdd" type="number" min="0" class="form-control input-sm" placeholder="Nivel" name="nivelAdd"></th>
    <td><input id="nombreAdd" type="text" class="form-control input-sm" placeholder="Nombre" name="nombreAdd"></td>
    <td><input id="precioAdd" type="number" min="0" class="form-control input-sm" placeholder="Precio" name="precioAdd"></td>
    <td><button id="addProducto" type="button" class="btn btn-outline-success" name="add">Añadir</button></td>
</tr>