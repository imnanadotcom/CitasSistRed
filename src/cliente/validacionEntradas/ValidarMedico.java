package cliente.validacionEntradas;

public class ValidarMedico {

    public static boolean esNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean validarNombre(String nombre) {
        return esNoVacio(nombre) && nombre.length() <= 100;
    }

    public static boolean validarCedula(String cedula) {
        return esNoVacio(cedula) && cedula.length() <= 50;
    }

    public static boolean validarEspecialidad(String especialidad) {
        return esNoVacio(especialidad) && especialidad.length() <= 50;
    }

    public static boolean validarCorreo(String correo) {
        return esNoVacio(correo) && correo.length() <= 100 && correo.contains("@");
    }

    public static boolean validarFormulario(String nombre, String curp, String telefono, String correo) {
        return validarNombre(nombre) &&
                validarCedula(curp) &&
                validarCorreo(correo);
    }
}

