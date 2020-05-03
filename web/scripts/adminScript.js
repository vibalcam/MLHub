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
    $('.datepicker-product').blur(function () {
        var action = "getMaxProductos";
        var fechaInicio = $('#inicioMaxProducto').val();
        var fechaFin = $('#finMaxProducto').val();
        $.post("inicio/admin/procesar", {
            action : action,
            fechaInicio : fechaInicio,
            fechaFin : fechaFin
        }, function (response) {
            $('#maxComprasProductos').html(response);
        })
    });
});