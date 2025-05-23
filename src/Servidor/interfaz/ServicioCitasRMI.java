package Servidor.interfaz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import common.CitaDTO;
import common.MedicoDTO;
import common.PacienteDTO;

public interface ServicioCitasRMI extends Remote {

    List<PacienteDTO> obtenerPacientes() throws RemoteException;
    void agregarPaciente(PacienteDTO paciente) throws RemoteException;
    void actualizarPaciente(PacienteDTO paciente) throws RemoteException;
    void eliminarPaciente(int idPaciente) throws RemoteException;
    PacienteDTO buscarPacientePorId(int idPaciente) throws RemoteException;
    List<String> obtenerNombresPacientes() throws RemoteException;

    List<MedicoDTO> obtenerMedicos() throws RemoteException;
    void agregarMedico(MedicoDTO medico) throws RemoteException;
    void actualizarMedico(MedicoDTO medico) throws RemoteException;
    void eliminarMedico(int idMedico) throws RemoteException;
    MedicoDTO buscarMedicoPorId(int idMedico) throws RemoteException;
    List<String> obtenerNombresMedicos() throws RemoteException;

    List<CitaDTO> obtenerCitas() throws RemoteException;
    void agregarCita(CitaDTO cita) throws RemoteException;
    void actualizarCita(CitaDTO cita) throws RemoteException;
    void eliminarCita(int idCita) throws RemoteException;
    CitaDTO buscarCitaPorId(int idCita) throws RemoteException;
}