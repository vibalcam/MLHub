$(document).ready(function () {
    // No permitir introducir ciertos caracteres y comprobar campos antes de enviar
    const PATTERN_NOT_ALLOWED = "[^A-Za-z-_:0-9]";
    $("#buttonAcc").click(function () {
        var usuario = $("#usuario").val().trim();
        var pwd = $("#pwd").val().trim();
        var error = $("#error");
        if(usuario.length === 0)
            error.html("Introduzca un usuario");
        else if(pwd.length === 0)
            error.html("Introduzca una contraseña");
        else if(usuario.search(PATTERN_NOT_ALLOWED) !== -1)
            error.html("Solo se permiten caracteres alfanuméricos, '-', '_' y ':'")
        else if(pwd.search(PATTERN_NOT_ALLOWED) !== -1)
            error.html("Usuario y/o contraseña incorrectos");
        else
            return;
        error.addClass("d-block");
        event.preventDefault();
    });
});