package Servidor;

import common.CitaDTO;
import common.MedicoDTO;
import common.PacienteDTO;
import Servidor.baseDatos.ConexionBD;
import common.interfaz.ServicioCitasRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ServidorOperacionesImpl extends UnicastRemoteObject implements ServicioCitasRMI {

    public ServidorOperacionesImpl() throws RemoteException {
        super();
    }

    @Override
    public List<PacienteDTO> obtenerPacientes() throws RemoteException {
        List<PacienteDTO> listaPacientes = new ArrayList<>();
        String sql = "SELECT id_paciente, nombre, curp, telefono, correo FROM Paciente";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PacienteDTO paciente = new PacienteDTO();
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setCurp(rs.getString("curp"));
                paciente.setTelefono(rs.getString("telefono"));
                paciente.setCorreo(rs.getString("correo"));
                listaPacientes.add(paciente);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al obtener pacientes: " + e.getMessage(), e);
        }
        return listaPacientes;
    }

    @Override
    public void agregarPaciente(PacienteDTO paciente) throws RemoteException {
        String sql = "INSERT INTO Paciente (nombre, curp, telefono, correo) VALUES (?, ?, ?, ?)";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getCurp());
            pstmt.setString(3, paciente.getTelefono());
            pstmt.setString(4, paciente.getCorreo());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setIdPaciente(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al agregar paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarPaciente(PacienteDTO paciente) throws RemoteException {
        String sql = "UPDATE Paciente SET nombre = ?, curp = ?, telefono = ?, correo = ? WHERE id_paciente = ?";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paciente.getNombre());
            pstmt.setString(2, paciente.getCurp());
            pstmt.setString(3, paciente.getTelefono());
            pstmt.setString(4, paciente.getCorreo());
            pstmt.setInt(5, paciente.getIdPaciente());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al actualizar paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarPaciente(int idPaciente) throws RemoteException {
        String sql = "DELETE FROM Paciente WHERE id_paciente = ?";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPaciente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al eliminar paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public PacienteDTO buscarPacientePorId(int idPaciente) throws RemoteException {
        String sql = "SELECT id_paciente, nombre, curp, telefono, correo FROM Paciente WHERE id_paciente = ?";
        PacienteDTO paciente = null;
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPaciente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente = new PacienteDTO();
                    paciente.setIdPaciente(rs.getInt("id_paciente"));
                    paciente.setNombre(rs.getString("nombre"));
                    paciente.setCurp(rs.getString("curp"));
                    paciente.setTelefono(rs.getString("telefono"));
                    paciente.setCorreo(rs.getString("correo"));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al buscar paciente por ID: " + e.getMessage(), e);
        }
        return paciente;
    }

    @Override
    public List<String> obtenerNombresPacientes() throws RemoteException {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM Paciente ORDER BY nombre ASC";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RemoteException("Error al obtener nombres de pacientes: " + e.getMessage(), e);
        }
        return nombres;
    }


    @Override
    public List<MedicoDTO> obtenerMedicos() throws RemoteException {
        List<MedicoDTO> listaMedicos = new ArrayList<>();
        String sql = "SELECT id_medico, nombre, especialidad, cedula, correo FROM Medico";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                MedicoDTO medico = new MedicoDTO();
                medico.setIdMedico(rs.getInt("id_medico"));
                medico.setNombre(rs.getString("nombre"));
                medico.setEspecialidad(rs.getString("especialidad"));
                medico.setCedula(rs.getString("cedula"));
                medico.setCorreo(rs.getString("correo"));
                listaMedicos.add(medico);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al obtener medicos: " + e.getMessage(), e);
        }
        return listaMedicos;
    }

    @Override
    public void agregarMedico(MedicoDTO medico) throws RemoteException {
        String sql = "INSERT INTO Medico (nombre, especialidad, cedula, correo) VALUES (?, ?, ?, ?)";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, medico.getNombre());
            pstmt.setString(2, medico.getEspecialidad());
            pstmt.setString(3, medico.getCedula());
            pstmt.setString(4, medico.getCorreo());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medico.setIdMedico(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al agregar medico: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarMedico(MedicoDTO medico) throws RemoteException {
        String sql = "UPDATE Medico SET nombre = ?, especialidad = ?, cedula = ?, correo = ? WHERE id_medico = ?";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, medico.getNombre());
            pstmt.setString(2, medico.getEspecialidad());
            pstmt.setString(3, medico.getCedula());
            pstmt.setString(4, medico.getCorreo());
            pstmt.setInt(5, medico.getIdMedico());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al actualizar medico: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarMedico(int idMedico) throws RemoteException {
        String sql = "DELETE FROM Medico WHERE id_medico = ?";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMedico);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al eliminar medico: " + e.getMessage(), e);
        }
    }

    @Override
    public MedicoDTO buscarMedicoPorId(int idMedico) throws RemoteException {
        String sql = "SELECT id_medico, nombre, especialidad, cedula, correo FROM Medico WHERE id_medico = ?";
        MedicoDTO medico = null;
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMedico);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    medico = new MedicoDTO();
                    medico.setIdMedico(rs.getInt("id_medico"));
                    medico.setNombre(rs.getString("nombre"));
                    medico.setEspecialidad(rs.getString("especialidad"));
                    medico.setCedula(rs.getString("cedula"));
                    medico.setCorreo(rs.getString("correo"));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al buscar medico por ID: " + e.getMessage(), e);
        }
        return medico;
    }

    @Override
    public List<String> obtenerNombresMedicos() throws RemoteException {
        List<String> nombres = new ArrayList<>();
        String sql = "SELECT nombre FROM Medico ORDER BY nombre ASC";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                nombres.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RemoteException("Error al obtener nombres de médicos: " + e.getMessage(), e);
        }
        return nombres;
    }

    @Override
    public List<CitaDTO> obtenerCitas() throws RemoteException {
        List<CitaDTO> listaCitas = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, " +
                "p.id_paciente, p.nombre AS nombre_paciente, " +
                "m.id_medico, m.nombre AS nombre_medico " +
                "FROM Cita c " +
                "JOIN Paciente p ON c.id_paciente = p.id_paciente " +
                "JOIN Medico m ON c.id_medico = m.id_medico";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CitaDTO cita = new CitaDTO();
                cita.setIdCita(rs.getInt("id_cita"));
                cita.setFecha(rs.getDate("fecha"));
                cita.setHora(rs.getTime("hora").toString().substring(0,5)); // HH:mm
                cita.setMotivo(rs.getString("motivo"));
                cita.setIdPaciente(rs.getInt("id_paciente"));
                cita.setNombrePaciente(rs.getString("nombre_paciente"));
                cita.setIdMedico(rs.getInt("id_medico"));
                cita.setNombreMedico(rs.getString("nombre_medico"));
                listaCitas.add(cita);
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al obtener citas: " + e.getMessage(), e);
        }
        return listaCitas;
    }

    @Override
    public void agregarCita(CitaDTO cita) throws RemoteException {
        String sql = "INSERT INTO Cita (fecha, hora, motivo, id_medico, id_paciente) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDate(1, new java.sql.Date(cita.getFecha().getTime()));
            pstmt.setTime(2, Time.valueOf(cita.getHora() + ":00")); // Asume formato HH:mm
            pstmt.setString(3, cita.getMotivo());
            pstmt.setInt(4, cita.getIdMedico());
            pstmt.setInt(5, cita.getIdPaciente());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cita.setIdCita(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al agregar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarCita(CitaDTO cita) throws RemoteException {
        String sql = "UPDATE Cita SET fecha = ?, hora = ?, motivo = ?, id_medico = ?, id_paciente = ? WHERE id_cita = ?";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, new java.sql.Date(cita.getFecha().getTime()));
            pstmt.setTime(2, Time.valueOf(cita.getHora() + ":00"));
            pstmt.setString(3, cita.getMotivo());
            pstmt.setInt(4, cita.getIdMedico());
            pstmt.setInt(5, cita.getIdPaciente());
            pstmt.setInt(6, cita.getIdCita());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al actualizar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarCita(int idCita) throws RemoteException {
        String sql = "DELETE FROM Cita WHERE id_cita = ?";
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al eliminar cita: " + e.getMessage(), e);
        }
    }

    @Override
    public CitaDTO buscarCitaPorId(int idCita) throws RemoteException {
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, " +
                "p.id_paciente, p.nombre AS nombre_paciente, " +
                "m.id_medico, m.nombre AS nombre_medico " +
                "FROM Cita c " +
                "JOIN Paciente p ON c.id_paciente = p.id_paciente " +
                "JOIN Medico m ON c.id_medico = m.id_medico " +
                "WHERE c.id_cita = ?";
        CitaDTO cita = null;
        try (Connection conn = new ConexionBD().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cita = new CitaDTO();
                    cita.setIdCita(rs.getInt("id_cita"));
                    cita.setFecha(rs.getDate("fecha"));
                    cita.setHora(rs.getTime("hora").toString().substring(0,5));
                    cita.setMotivo(rs.getString("motivo"));
                    cita.setIdPaciente(rs.getInt("id_paciente"));
                    cita.setNombrePaciente(rs.getString("nombre_paciente"));
                    cita.setIdMedico(rs.getInt("id_medico"));
                    cita.setNombreMedico(rs.getString("nombre_medico"));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Error de SQL al buscar cita por ID: " + e.getMessage(), e);
        }
        return cita;
    }
}