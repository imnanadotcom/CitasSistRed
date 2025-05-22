package common;

import java.io.Serializable;
import java.util.Date;

public class CitaDTO implements Serializable {
    private static final long serialVersionUID = 3L;

    private int idCita;
    private Date fecha;
    private String hora;
    private String motivo;
    private int idPaciente;
    private int idMedico;

    private String nombrePaciente;
    private String nombreMedico;

    public CitaDTO() {}

    public CitaDTO(int idCita, Date fecha, String hora, String motivo, int idPaciente, int idMedico, String nombrePaciente, String nombreMedico) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.nombrePaciente = nombrePaciente;
        this.nombreMedico = nombreMedico;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    @Override
    public String toString() {
        return "CitaDTO{" +
                "idCita=" + idCita +
                ", fecha=" + fecha +
                ", hora='" + hora + '\'' +
                ", motivo='" + motivo + '\'' +
                ", nombrePaciente='" + nombrePaciente + '\'' +
                ", nombreMedico='" + nombreMedico + '\'' +
                '}';
    }
}