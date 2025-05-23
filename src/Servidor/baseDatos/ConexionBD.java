package Servidor.baseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private Connection conexion;

    public ConexionBD() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:8889/sistema_medico";
        String usuario = "root";
        String password = "root";

        conexion = DriverManager.getConnection(url, usuario, password);
        System.out.println("Conexión a la base de datos establecida.");
    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}