var script = document.createElement('script');
script.src = 'https://code.jquery.com/jquery-3.4.1.min.js';
script.type = 'text/javascript';
document.getElementsByTagName('head')[0].appendChild(script);

const PATTERN_NOT_ALLOWED = "[^A-Za-z-_:0-9]";
var fin;

function comprobacion2() {
    var usuario = document.getElementById("usuario").value;
    var pwd = document.getElementById("pwd").value;

    if(fin){
        return true;
    }
    var error = $('#error');

    if ((usuario.length === 0) || (pwd.length === 0)) {
        error.html("Por favor, rellene los campos obligatorios");
        error.removeClass("d-none");
        return false;
    }else if((usuario.search(PATTERN_NOT_ALLOWED) !== -1) || (pwd.search(PATTERN_NOT_ALLOWED) !== -1)){
        error.html("Solo se permiten caracteres alfanuméricos, '-', '_' y ':' para el usuario y la contraseña");
        error.removeClass("d-none");
        return false;
    }else{
        return true;
    }
}

function registrar(){
    fin = false;
}

function cancelar(){
    fin = true;
}

function borrar(){
    document.getElementById("usuario").value = "";
    document.getElementById("pwd").value = "";
    document.getElementById("name").value = "";
    document.getElementById("lastname").value = "";
    document.getElementById("error").innerText = "";
    return false;
}