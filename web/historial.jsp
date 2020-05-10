<%@ page import="dominio.Usuario" %>
<%@ page import="dao.MLDao" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap css -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!-- Custom css -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/styleSidebar.css">

    <title>Portal de Admin</title>
</head>

<body>
<div class="wrapper">
    <!-- Navigation  -->
    <div id="content">
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <form class="row form-inline ml-4 d-none d-lg-inline" id="formBuscar" name="formBuscar" method="get" action="${pageContext.request.contextPath}/inicio">
                    <input class="form-control mr-sm-2 col-7" type="search" name="searchName" required placeholder="Buscar" aria-label="Buscar">
                    <button class="btn btn-success" type="submit" name="action" value="searchEntry">Buscar</button>
                </form>

                <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fas fa-align-justify"></i>
                    Mostrar navegación
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="nav navbar-nav ml-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/inicio">Inicio</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/inicio/subscripcion">Subscripción</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="${pageContext.request.contextPath}/inicio/historial">Historial</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/inicio/modificar">Usuario</a>
                        </li>
                        <li class="nav-item">
                            <form id="formCerrarSesion" name="formCerrarSesion" method="post" action="${pageContext.request.contextPath}/inicio">
                                <a href="javascript:$('#formCerrarSesion').submit();" id="linkCerrarSesion" class="nav-link">Cerrar sesión</a>
                                <input type="hidden" name="action" value="cerrarSesion">
                            </form>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Page Content  -->

        <div class="card text-center">
            <div class="card-header">
                <h4><c:out value="Historial"/></h4>
            </div>
            <div class="card-body">
                <table class="table">
                    <thead class="thead-dark">
                    <tr>
                        <th scope="col">Fecha</th>
                        <th scope="col">Producto</th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach items = "${compras}" var = "compra">
                        <tr>
                            <td><fmt:formatDate value="${compra.fecha.time}" type="date" dateStyle="short" /></td>
                            <td><c:out value="${compra.nombre}"/></td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>

    </div>

    <!-- Icons -->
    <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/solid.js" integrity="sha384-tzzSw1/Vo+0N5UhStP3bvwWPq+uvzCMfrN1fEFe+xBmv1C/AtVX5K0uZtmcHitFZ" crossorigin="anonymous"></script>
    <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js" integrity="sha384-6OIrr52G08NpOFSZdxxz1xdNSndlD4vdcf/q2myIUVO0VsqaGHJsB0RaBE01VTOY" crossorigin="anonymous"></script>
    <!-- JQuery -->
    <script src="${pageContext.request.contextPath}/scripts/jquery-3.3.1.js"></script>
    <!-- Bootstrap js -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>


</div>
</body>
</html>