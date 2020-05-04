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

    // Load listeners
    function loadListeners() {
        $('.input-change').change(function () {
            error.html("Se ha realizado una modificación en los productos");
            error.addClass("bg-warning");
            error.removeClass("bg-danger bg-success d-none");
        });

        // Modificar productos
        $('.changeProducto').click(function () {
            var action = "changeProducto";
            var id = $(this).val();
            var nombre = $('#nombreProducto' + id).val();
            var precio = $('#precioProducto' + id).val();
            var accessLevel = $('#nivelProducto' + id).val();

            $.post("/MLHub/inicio/admin", {
                action : action,
                id : id,
                nombre : nombre,
                precio : precio,
                accessLevel : accessLevel
            }, function (response) {
                successChange(response);
            }).fail(function () {
                errorChange();
            });
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
                errorChange();
            });
        });

        // Add producto
        $('#addProducto').click(function () {
            var action = "addProducto";
            var nombre = $('#nombreAdd').val();
            var precio = $('#precioAdd').val();
            var accessLevel = $('#nivelAdd').val();
            $.post("/MLHub/inicio/admin", {
                action : action,
                nombre : nombre,
                precio : precio,
                accessLevel : accessLevel
            }, function (response) {
                successChange(response);
            }).fail(function () {
                errorChange();
            });
        });
    }


    var error = $('#textChgProductos');
    function successChange(response) {
        $('#mostrarProductos').html(response);
        error.html("Cambio realizado con éxito");
        error.removeClass("bg-warning bg-danger");
        error.addClass("bg-success");
        loadListeners();
    }
    function errorChange() {
        error.html("Error al realizar el cambio: consulte con soporte");
        error.removeClass("bg-warning bg-success");
        error.addClass("bg-danger");
    }
    loadListeners();
});