const PATTERN_NOT_ALLOWED = "[^A-Za-z-_:0-9]";
const PATTERN_NOT_ALLOWED2 = "[0-9]";

function comprobacion() {
    var proyecto = document.getElementById("nuevoProyecto").value;

    if (proyecto === "") {
        document.getElementById("error").innerText = "Por favor, rellene el nombre"
        return false;
    }else if(proyecto.search(PATTERN_NOT_ALLOWED) !== -1){
        document.getElementById("error").innerText = "Solo se permiten caracteres alfanuméricos, '-', '_' y ':'";
        return false;
    }else{
        return true;
    }
}

function comprobacion2() {
    console.log("Hola");

    var nombre = document.getElementById("nombre").value;
    var eficacia = document.getElementById("eficacia").value;
    var tiempo = document.getElementById("tiempo").value;

    if ((nombre === "") || (eficacia === "") || (tiempo === "")) {
        document.getElementById("error").innerText = "Por favor, rellene los campos que faltan"
        return false;
    }else if(nombre.search(PATTERN_NOT_ALLOWED) !== -1){
        document.getElementById("error").innerText = "Solo se permiten caracteres alfanuméricos, '-', '_' y ':' en el nombre";
        return false;
    }else if((eficacia.search(PATTERN_NOT_ALLOWED2) !== -1) || (eficacia.search(PATTERN_NOT_ALLOWED2) !== -1)){
        document.getElementById("error").innerText = "Solo se permiten numeros para las casillas de tiempo y eficacia";
        return false;
    } else {
        return true;
    }
}