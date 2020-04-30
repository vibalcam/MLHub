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
    
});