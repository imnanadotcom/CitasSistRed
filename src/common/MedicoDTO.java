package common;

import java.io.Serializable;

public class MedicoDTO implements Serializable {
    private static final long serialVersionUID = 2L;

    private int idMedico;
    private String nombre;
    private String especialidad;
    private String cedula;
    private String correo;

    public MedicoDTO() {}

    public MedicoDTO(int idMedico, String nombre, String especialidad, String cedula, String correo) {
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.cedula = cedula;
        this.correo = correo;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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