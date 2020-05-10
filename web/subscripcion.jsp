<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8">
        <title>MLHub Subscripciones</title>
        <!-- General css -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/styles.css"/>
        <!-- Specific css -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/styleSubscription.css"/>
    </head>
    <body>
        <div id="content" class="center">
            <h1 id="title" class="textCenter">Elige tu subscripción</h1>
            <br>
            <form id="form-precios" name="form-precios" method="post"
                  action="${pageContext.request.contextPath}/inicio/subscripcion">
                <table class="center">
                    <thead>
                        <tr>
                            <th scope="col">Características</th>
                            <c:forEach var="subs" items="${subscripciones}">
                                <th scope="col"><c:out value="${subs.nombre}"/></th>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th scope="row">Subir código</th>
                            <c:forEach var="subs" items="${subscripciones}">
                                <td>
                                    <c:choose>
                                        <c:when test="${subs.accessLevel.subirCodigo}">
                                            <div class="icon icon-tick center">
                                                <i class="fas fa-check fa-fw"></i>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="icon icon-x center">
                                                <i class="fas fa-times fa-fw"></i>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <th scope="row">Subir resultados</th>
                            <c:forEach var="subs" items="${subscripciones}">
                                <td>
                                    <c:choose>
                                        <c:when test="${subs.accessLevel.subirResultados}">
                                            <div class="icon icon-tick center">
                                                <i class="fas fa-check fa-fw"></i>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="icon icon-x center">
                                                <i class="fas fa-times fa-fw"></i>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <th scope="row">Acceso a Resultados Ajenos</th>
                            <c:forEach var="subs" items="${subscripciones}">
                                <td>
                                    <c:choose>
                                        <c:when test="${subs.accessLevel.accesoResultados}">
                                            <div class="icon icon-tick center">
                                                <i class="fas fa-check fa-fw"></i>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="icon icon-x center">
                                                <i class="fas fa-times fa-fw"></i>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <th>Precio</th>
                            <c:forEach var="subs" items="${subscripciones}">
                                <td class="center">
                                    <button class="boton-precio" type="submit" name="subsId" value="${subs.id}">
                                        <c:choose>
                                            <c:when test="${subs.porcentajeOferta != 0}">
                                                <span class="precioAnterior"><c:out value="${subs.precio}"/> €/mes</span>
                                                <br>
                                                <br>
                                                <span class="precioOferta"><c:out value="${subs.precioReal}"/> €/mes</span>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${subs.precio}"/> €/mes
                                            </c:otherwise>
                                        </c:choose>
                                    </button>
                                </td>
                            </c:forEach>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>

        <c:if test="${not empty requestScope.error}">
            <br>
            <div id="error" class="center"><c:out value="${requestScope.error}"/></div>
        </c:if>

        <br>
        <div class="textCenter" >
            <a id="vueltaInicio" href="${pageContext.request.contextPath}/inicio">Volver al inicio</a>
        </div>


        <!-- JQuery -->
        <script src="${pageContext.request.contextPath}/scripts/jquery-3.3.1.js"></script>
        <!-- Icons -->
        <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/solid.js" integrity="sha384-tzzSw1/Vo+0N5UhStP3bvwWPq+uvzCMfrN1fEFe+xBmv1C/AtVX5K0uZtmcHitFZ" crossorigin="anonymous"></script>
        <script defer src="https://use.fontawesome.com/releases/v5.0.13/js/fontawesome.js" integrity="sha384-6OIrr52G08NpOFSZdxxz1xdNSndlD4vdcf/q2myIUVO0VsqaGHJsB0RaBE01VTOY" crossorigin="anonymous"></script>
    </body>
</html>