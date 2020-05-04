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
            <!-- Sidebar  -->
            <nav id="sidebar" class="bg-dark text-white">
                <div class="sidebar-header">
                    <h3>Admin Portal</h3>
                    <strong>MLH<i class="fas fa-users-cog"></i></strong>
                </div>
    
                <ul class="list-unstyled components">
                    <li>
                        <a href="#estadisticas" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                            <i class="fas fa-calculator"></i>
                            Estadísticas
                        </a>
                        <ul class="collapse list-unstyled sidebar-sections" id="estadisticas">
                            <li>
                                <a href="#estadisticaProductos">Productos</a>
                            </li>
                            <li>
                                <a href="#estadisticaFechas">Fechas</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#catalogo" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                            <i class="fas fa-copy"></i>
                            Catálogo
                        </a>
                        <ul class="collapse list-unstyled sidebar-sections" id="catalogo">
                            <li>
                                <a href="#catalogoDisponibles">Disponibles</a>
                            </li>
                            <li>
                                <a href="#catalogoOfertas">Ofertas</a>
                            </li>
                        </ul>
                    </li>
                </ul>
    
                <ul id="shortcuts" class="list-unstyled">
                    <li>
                        <a href="#" class="quick-links bg-secondary text-white">Vuelta al inicio</a>
                    </li>
                </ul>
            </nav>
    
            <!-- Navigation  -->
            <div id="content">
                <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                    <div class="container-fluid">
                        <button type="button" id="sidebarCollapse" class="btn btn-secondary">
                            <i class="fas fa-align-left"></i>
                            <span>Toggle Sidebar</span>
                        </button>

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
                                <li class="nav-item active">
                                    <a class="nav-link" href="${pageContext.request.contextPath}/inicio/admin">Cuenta</a>
                                </li>
                                <li class="nav-item">
                                    <form id="formCerrarSesion" name="formCerrarSesion" method="post" action="${pageContext.request.contextPath}/inicio">
                                        <a class="nav-link" href="#">Cerrar sesión</a>
<%--                                        todo submit with jquery--%>
                                    </form>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <!-- Page Content  -->
                <h2>Estadísticas</h2>
                <br>

                <div class="row overflow-hidden">
                    <div class="col">
                        <div class="row" id="estadisticaProductos">
                            <h3 class="col">Productos más comprados</h3>
                            <div class="w-100 d-block d-lg-none"></div>
                            <form class="col form-row justify-content-end" name="dateProductos" id="dateProductos">
                                <div class="col-lg-auto col-md-6">
                                    <input id="inicioMaxProducto" name="inicioMaxProducto" type="date" class="form-control datepickerStart datepicker-product" placeholder="Inicio">
                                </div>
                                <div class="col-lg-auto col-md-6">
                                    <input id="finMaxProducto" name="finMaxProducto" type="date" class="form-control datepickerEnd datepicker-product" placeholder="Final">
                                </div>
                            </form>
                        </div>
                        <div class="border border-dark rounded mt-2">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Nombre</th>
                                        <th scope="col">Cantidad vendida</th>
                                    </tr>
                                </thead>
                                <tbody id="maxComprasProductos">
                                    <jsp:include page="/admin/maxComprasProductos.jsp"/>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="w-100 d-block d-xl-none"></div>
                    <br class="w-100 d-block d-xl-none">

                    <div class="col">
                        <div class="row" id="estadisticaFechas">
                            <h3 class="col">Volumen de compras diarias</h3>
                            <div class="w-100 d-block d-lg-none"></div>
                            <form class="col form-row justify-content-end" name="dateFechas" id="dateFechas">
                                <div class="col-lg-auto col-md-6">
                                    <input id="inicioMaxFecha" name="inicio" type="date" class="datepicker-fecha form-control datepickerStart" placeholder="Inicio">
                                </div>
                                <div class="col-lg-auto col-md-6">
                                    <input id="finMaxFecha" name="fin" type="date" class="datepicker-fecha form-control datepickerEnd" placeholder="Final">
                                </div>
                            </form>
                        </div>
                        <div class="border border-dark rounded mt-2">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Fecha</th>
                                        <th scope="col">Cantidad vendida</th>
                                    </tr>
                                </thead>
                                <tbody id="maxComprasFechas">
                                    <jsp:include page="/admin/maxComprasFechas.jsp"/>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                
                <div class="line bg-dark"></div>
    
                
                <h2>Catálogo</h2>
                <br>
                
                <div class="row">
                    <div class="col">
                        <div id="catalogoDisponibles" class="row">
                            <h3 class="col">Productos disponibles</h3>
                            <div class="w-100 d-block d-lg-none"></div>
<%--                            todo si me apetece o quitar sino--%>
                            <form class="mr-2 col form-row justify-content-end" name="buscarProductos" id="buscarProductos">
                                <div class="col">
                                    <input type="text" name="nombre" class="form-control" placeholder="Nombre">
                                </div>
                                
                                <button type="submit" class="btn btn-outline-success col-auto">Buscar</button>
                            </form>
                        </div>
                        <div class="my-2 p-1 rounded bg-info" id="textChgProductos">
                            Para crear nuevos niveles de acceso debe contactar con soporte
                        </div>
                        <div class="border border-dark rounded mt-2">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th scope="col">Nivel</th>
                                        <th scope="col">Nombre</th>
                                        <th scope="col">Precio (€)</th>
                                        <th scope="col">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="mostrarProductos">
                                    <jsp:include page="/admin/mostrarProductos.jsp"/>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    
                    <div class="w-100 d-block d-xl-none"></div>
                    <br class="w-100 d-block d-xl-none">
    
                    <div class="col">
                        <div id="catalogoOfertas" class="row">
                            <h3 class="col">Ofertas disponibles&nbsp&nbsp&nbsp&nbsp&nbsp</h3>
                            <div class="w-100 d-block d-lg-none"></div>
                            <form class="col" name="addOferta" id="addOferta">
                                <div class="mr-2 form-row justify-content-end">
                                    <div id="inputId" class="col">
                                        <input type="number" name="id" class="form-control" placeholder="Id">
                                    </div>
                                    <div class="d-inline col">
                                        <input type="number" name="oferta" min="0" max="100" class="form-control" placeholder="% Oferta">
                                    </div>
                                    <button type="submit" class="btn btn-outline-success col">Hacer oferta</button>
                                </div>
                            </form>
                        </div>
                        <div class="border border-dark rounded mt-2">
                            <form id="formOfertas" name="formOfertas" method="post" action="${pageContext.request.contextPath}/inicio/admin">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th scope="col">Nombre</th>
                                            <th scope="col">Precio (€)</th>
                                            <th scope="col">Descuento</th>
                                            <th scope="col" class="d-none d-sm-block">Precio descontado (€)</th>
                                            <th scope="col">Eliminar</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <jsp:include page="/admin/mostrarOfertas.jsp"/>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
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

        <!-- Custom js -->
        <script src="${pageContext.request.contextPath}/scripts/adminScript.js"></script>
    </body>
</html>