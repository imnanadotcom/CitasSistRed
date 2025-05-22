package common;

import java.io.Serializable;

public class PacienteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idPaciente;
    private String nombre;
    private String curp;
    private String telefono;
    private String correo;

    public PacienteDTO() {}

    public PacienteDTO(int idPaciente, String nombre, String curp, String telefono, String correo) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.curp = curp;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}