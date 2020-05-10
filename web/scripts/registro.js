const PATTERN_NOT_ALLOWED = "[^A-Za-z-_:0-9]";
var fin;

function comprobacion2() {
    var usuario = document.getElementById("usuario").value;
    var pwd = document.getElementById("pwd").value;

    if(fin){
        return true;
    }

    if ((usuario === "") || (pwd === "")) {
        document.getElementById("error").innerText = "Por favor, rellene los campos obligatorios"
        return false;
    }else if((usuario.search(PATTERN_NOT_ALLOWED) !== -1) || (pwd.search(PATTERN_NOT_ALLOWED) !== -1)){
        document.getElementById("error").innerText = "Solo se permiten caracteres alfanuméricos, '-', '_' y ':' para el usuario y la contraseña";
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