package cliente.validacionEntradas;

public class ValidarPaciente {

    public static boolean esNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean validarNombre(String nombre) {
        return esNoVacio(nombre) && nombre.length() <= 100;
    }

    public static boolean validarCurp(String curp) {
        return esNoVacio(curp) && curp.length() == 18;
    }

    public static boolean validarTelefono(String telefono) {
        return esNoVacio(telefono) && telefono.length() == 10 && telefono.matches("\\d+");
    }

    public static boolean validarCorreo(String correo) {
        return esNoVacio(correo) && correo.length() <= 100 && correo.contains("@");
    }

    public static boolean validarFormulario(String nombre, String curp, String telefono, String correo) {
        return validarNombre(nombre) &&
                validarCurp(curp) &&
                validarTelefono(telefono) &&
                validarCorreo(correo);
    }
}
