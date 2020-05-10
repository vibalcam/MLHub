<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <!-- Bootstrap css -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>MLHub</title>
    <link rel="stylesheet" href="styles/styles.css"/>
</head>

<body class="offset-xl-3 col-xl-6 offset-sm-1 col-sm-10">
<div class="jumbotron bg-dark text-white">
    <div class="container text-center row align-items-center justify-content-center">
        <div class="col-xl-6 col-md-12">
            <h1 class="text-center">MLHub</h1>
            <p class="lead">
                El lugar para los developers de Machine Learning
            </p>
        </div>
        <img class="offset-xl-1 col-xl-4 col-sm-6 img-fluid rounded" src="${pageContext.request.contextPath}/images/logo.jpg" alt="Logo">
    </div>
</div>

<div class="p-5 bg-light card border border-dark rounded">
    <div class = "container">
        <h1 class = "header">Editar Usuario</h1>
    </div>

    <hr>

    <form class="card-body" action="${pageContext.request.contextPath}/inicio/modificar" method="post" name="inicio"
          id="inicioForm" onsubmit="return comprobacion2();">
        <div class="form-group col-sm-auto">
            <label for="name">Nombre: </label>
            <input class="form-control input-credentials" minlength="1" maxlength="${initParam.maxLength}"
                   type="text" name="name" id="name" placeholder="Nombre" value="${sessionScope.userLogged.nombre}">
        </div>

        <div class="form-group col-sm-auto">
            <label for="lastname">Apellidos: </label>
            <input class="form-control input-credentials" minlength="1" maxlength="${initParam.maxLength}"
                   type="text" name="lastname" id="lastname" placeholder="Apellidos" value="${sessionScope.userLogged.apellidos}">
        </div>

        <div class="form-group col-sm-auto">
            <label for="usuario"><span class="text-danger">*</span>Usuario: </label>
            <input class="form-control input-credentials" minlength="1" maxlength="${initParam.maxLength}"
                   type="text" name="usuario" id="usuario" placeholder="Contraseña">
        </div>

        <div class="form-group col-sm-auto">
            <label for="pwd"><span class="text-danger">*</span>Contraseña: </label>
            <input class="form-control input-credentials" minlength="1" maxlength="${initParam.maxLength}"
                   type="password" name="pwd" id="pwd" placeholder="Contraseña">
        </div>

        <p class = "p-3 mb-2 text-primary">Los campos con asterisco son obligatorios</p>

        <div class="form-group col-sm-auto">
            <button class="btn btn-success" type="submit" name="peticion" value="save" onclick="return registrar();">Guardar Cambios</button>
            <button class="btn btn-warning" onclick="return borrar();">Borrar</button>
            <button class="btn btn-danger" type="submit" name="peticion" value="delete" onclick="return cancelar();">Eliminar Perfil</button>
        </div>

        <c:choose>
            <c:when test="${not empty requestScope.error}">
                <div id="error" class="bg-danger text-light font-weight-bold p-1 px-4 m-2 rounded">${requestScope.error}</div>
            </c:when>
            <c:otherwise>
                <div id="error" class="bg-danger text-light font-weight-bold p-1 px-4 m-2 rounded d-none">${requestScope.error}</div>
            </c:otherwise>
        </c:choose>

    </form>

</div>

<!-- JQuery -->
<script src="scripts/jquery-3.3.1.js"></script>
<!-- Bootstrap js -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

<!-- Custom js -->
<script src="scripts/registro.js"></script>
</body>
</html>
