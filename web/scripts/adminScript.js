$(document).ready(function () {
    // Muestra/oculta sidebar añadiendo la clase activa al sidebar
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });

    // Insertamos los mínimos y máximos en los datepickers
    $('.datepickerStart').focus(function() {
        console.log($('.datepickerEnd').val());
        $('.datepickerStart').attr("max",$('.datepickerEnd').val());
    });
    $('.datepickerEnd').focus(function() {
        console.log($('.datepickerStart').val());
        $('.datepickerEnd').attr("min",$('.datepickerStart').val());
    });

    // AJAX estadísticas

    // Productos mas comprados
    $('.datepicker-product').blur(function () {
        var action = "getMaxProductos";
        var fechaInicio = $('#inicioMaxProducto').val();
        var fechaFin = $('#finMaxProducto').val();
        $.post("/MLHub/inicio/admin", {
            action : action,
            fechaInicio : fechaInicio,
            fechaFin : fechaFin
        }, function (response) {
            $('#maxComprasProductos').html(response);
        })
    });

    // Fechas con mas compras
    $('.datepicker-fecha').blur(function () {
        var action = "getMaxFechas";
        var fechaInicio = $('#inicioMaxFecha').val();
        var fechaFin = $('#finMaxFecha').val();
        $.post("/MLHub/inicio/admin", {
            action : action,
            fechaInicio : fechaInicio,
            fechaFin : fechaFin
        }, function (response) {
            $('#maxComprasFechas').html(response);
        })
    });

    // Change productos
    var errorProductos = $('#textChgProductos');
    const MSG_ERROR_PRODUCTOS = "Error al realizar el cambio: consulte con soporte";
    const MSG_PARAM_PRODUCTOS = "Faltan parámetros por introducir";
    function successChange(response) {
        $('#mostrarProductos').html(response);
        $('#nombreBusquedaProductos').html("");
        errorProductos.html("Cambio realizado con éxito");
        errorProductos.removeClass("bg-warning bg-danger");
        errorProductos.addClass("bg-success");
        loadProductosListeners();
    }
    function errorChange(errorText) {
        errorProductos.html(errorText);
        errorProductos.removeClass("bg-warning bg-success");
        errorProductos.addClass("bg-danger");
    }

    // Load listeners
    function loadProductosListeners() {
        $('.input-change').change(function () {
            errorProductos.html("Se ha realizado una modificación en los productos");
            errorProductos.addClass("bg-warning");
            errorProductos.removeClass("bg-danger bg-success d-none");
        });

        // Modificar productos
        $('.changeProducto').click(function () {
            var action = "changeProducto";
            var id = $(this).val();
            var nombre = $('#nombreProducto' + id).val().trim();
            var precio = $('#precioProducto' + id).val();
            var accessLevel = $('#nivelProducto' + id).val();

            if(nombre.length !== 0 && precio.length!==0 && precio>=0 && accessLevel.length !==0) {
                $.post("/MLHub/inicio/admin", {
                    action: action,
                    id: id,
                    nombre: nombre,
                    precio: precio,
                    accessLevel: accessLevel
                }, function (response) {
                    successChange(response);
                }).fail(function () {
                    errorChange(MSG_ERROR_PRODUCTOS);
                });
            } else
                errorChange(MSG_PARAM_PRODUCTOS);
        });

        // Delete producto
        $('.deleteProducto').click(function () {
            var action = "deleteProducto";
            var id = $(this).val();
            $.post("/MLHub/inicio/admin", {
                action : action,
                id : id
            }, function (response) {
                successChange(response);
            }).fail(function () {
                errorChange(MSG_ERROR_PRODUCTOS);
            });
        });

        // Add producto
        $('#addProducto').click(function () {
            var action = "addProducto";
            var nombre = $('#nombreAdd').val().trim();
            var precio = $('#precioAdd').val();
            var accessLevel = $('#nivelAdd').val();

            if(nombre.length !== 0 && precio.length!==0 && precio>=0 && accessLevel.length !==0) {
                $.post("/MLHub/inicio/admin", {
                    action: action,
                    nombre: nombre,
                    precio: precio,
                    accessLevel: accessLevel
                }, function (response) {
                    successChange(response);
                }).fail(function () {
                    errorChange(MSG_ERROR_PRODUCTOS);
                });
            } else
                errorChange(MSG_PARAM_PRODUCTOS);
        });
    }

    function loadOfertasListeners() {
        // Eliminar oferta
        $('.deleteOferta').click(function () {
            var action = "deleteOferta";
            var id = $(this).val();
            $.post("/MLHub/inicio/admin", {
                action : action,
                id : id
            }, function (response) {
                $('#mostrarOfertas').html(response);
                loadOfertasListeners();
            });
        });
    }

    // Add oferta
    var errorOfertas = $('#textChgOfertas');
    $('#addOferta').click(function () {
        var action = "addOferta";
        var nombre = $('#nombreOfertaAdd').val().trim();
        var oferta = $('#porcentajeOfertaAdd').val();

        if(nombre.length !== 0 && oferta.length!==0 && oferta>=0 && oferta<=100) {
            $.post("/MLHub/inicio/admin", {
                action: action,
                nombre: nombre,
                oferta: oferta
            }, function (response) {
                $('#mostrarOfertas').html(response);
                loadOfertasListeners();
                errorOfertas.removeClass("d-none bg-warning bg-danger");
                errorOfertas.addClass("bg-success");
                errorOfertas.html("Cambio realizado con éxito");
            }).fail(function () {
                errorOfertas.removeClass("d-none bg-warning bg-success");
                errorOfertas.addClass("bg-danger");
                errorOfertas.html("El producto introducido no existe");
            });
        } else {
            if(nombre.length === 0)
                errorOfertas.html("Debe introducir el nombre del producto a ofertar");
            else
                errorOfertas.html("El porcentaje de oferta debe de estar entre 0 y 100");
            errorOfertas.removeClass("d-none bg-warning bg-success");
            errorOfertas.addClass("bg-danger");
        }
    });

    // Busar productos
    $('#searchProductos').click(function () {
        var action = "searchProductos";
        var filtro = $('#nombreBusquedaProductos').val().trim();

        $.post("/MLHub/inicio/admin", {
            action: action,
            filtro: filtro
        }, function (response) {
            $('#mostrarProductos').html(response);
            loadProductosListeners();
        });
    });

    loadProductosListeners();
    loadOfertasListeners();
});