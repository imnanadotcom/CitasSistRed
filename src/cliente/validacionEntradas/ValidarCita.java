package cliente.validacionEntradas;

import java.util.Calendar;
import java.util.Date;

public class ValidarCita {

    public static boolean validarPaciente(String paciente) {
        return paciente != null && !paciente.trim().isEmpty();
    }

    public static boolean validarMedico(String medico) {
        return medico != null && !medico.trim().isEmpty();
    }

    public static boolean validarFecha(Date fecha) {
        if (fecha == null) return false;

        // Validar que la fecha no sea pasada (menor a hoy)
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        return !fecha.before(calHoy.getTime());
    }

    public static boolean validarHora(int hora) {
        return hora >= 0 && hora <= 23;
    }

    public static boolean validarMinuto(int minuto) {
        // Validamos que sea múltiplo de 10 y esté entre 0 y 50
        return minuto >= 0 && minuto <= 50 && minuto % 10 == 0;
    }

    public static boolean validarMotivo(String motivo) {
        if (motivo == null) return false;
        String m = motivo.trim();
        return !m.isEmpty() && m.length() <= 200;
    }

    // Validación completa
    public static boolean validarCita(String paciente, String medico, Date fecha, int hora, int minuto, String motivo) {
        return validarPaciente(paciente) &&
                validarMedico(medico) &&
                validarFecha(fecha) &&
                validarHora(hora) &&
                validarMinuto(minuto) &&
                validarMotivo(motivo);
    }
}
