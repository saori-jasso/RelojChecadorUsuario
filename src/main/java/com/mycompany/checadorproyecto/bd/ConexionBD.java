package com.mycompany.checadorproyecto.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    // ¡CRÍTICO!: Aquí pones la IP de tu máquina Servidora (la que tiene XAMPP)
    // Recuerda cambiar este número mañana por el que te asigne el Access Point físico
    private static final String URL = "jdbc:mysql://192.168.0.182:3306/checador?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    
    // Usuario por defecto de XAMPP
    private static final String USUARIO = "root"; 
    
    // Contraseña por defecto de XAMPP (vacía, sin espacios)
    private static final String PASSWORD = ""; 

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("¡Conexión remota exitosa al MySQL del Servidor!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión en el cliente: " + e.getMessage());
        }
        return conexion;
    }
}