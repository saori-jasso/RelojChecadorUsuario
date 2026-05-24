/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadorproyecto.bd;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author soporte
 */
public class ConexionBD {
    private static final String URL = "jdbc:mysql://relojchecador.c3okscuymwtz.us-east-2.rds.amazonaws.com:3306/checador";
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "password123"; 

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar usuarios con AWS: " + e.getMessage());
        }
        return conexion;
    }
    
}
