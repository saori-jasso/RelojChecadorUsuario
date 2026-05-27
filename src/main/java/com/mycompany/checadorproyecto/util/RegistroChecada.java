package com.mycompany.checadorproyecto.util;

import com.mycompany.checadorproyecto.bd.ConexionBD;
import java.sql.*;
import java.time.LocalTime;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;

public class RegistroChecada {

    private int idChecador;
    private RegistroEntradaSalida registroES;

    public RegistroChecada(int idChecador) {
        this.idChecador = idChecador;
        this.registroES = new RegistroEntradaSalida(idChecador);
    }

    // =========================================
    // REGISTRAR CHECADA (desde huella o matrícula)
    // =========================================
    public void registrarChecada(Connection conexion, int matricula) {
        try {
            java.sql.Date fechaActual =
                    new java.sql.Date(System.currentTimeMillis());
            Time horaActual =
                    new Time(System.currentTimeMillis());

            String sqlEmpleado =
                    "SELECT puesto FROM Empleados WHERE matricula = ?";

            PreparedStatement psEmpleado =
                    conexion.prepareStatement(sqlEmpleado);

            psEmpleado.setInt(1, matricula);

            ResultSet rsEmpleado = psEmpleado.executeQuery();

            if (!rsEmpleado.next()) {
                return;
            }

            String puesto =
                    rsEmpleado.getString("puesto").toLowerCase();
            System.out.println("PUESTO: " + puesto);

            if (puesto.contains("profesor") || puesto.contains("ambos")) {
                procesarHorarioClase(conexion, matricula, fechaActual, horaActual);
            }

            if (puesto.contains("administrativo") || puesto.contains("ambos")) {
                procesarHorarioAdministrador(conexion, matricula, fechaActual, horaActual);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================
    // REGISTRAR POR MATRÍCULA (desde UI)
    // =========================================
    public void registrarPorMatricula(
        String matriculaTexto,
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck,
        InterfazChecador interfaz
    ) {
        if (matriculaTexto == null || matriculaTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa una matrícula.");
            return;
        }

        int matricula;
        try {
            matricula = Integer.parseInt(matriculaTexto.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La matrícula debe ser numérica.");
            return;
        }

        String sql = "SELECT matricula FROM Empleados WHERE matricula = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, matricula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                registrarChecada(conexion, matricula);
                interfaz.actualizarInterfazCorrecta(
                        matricula, txtID, txtFecha, txtHoraRegistrada, lblCheck);
            } else {
                lblCheck.setText("DENEGADO");
                lblCheck.setForeground(Color.RED);
                interfaz.limpiarCamposDespues(
                        txtID, txtFecha, txtHoraRegistrada, lblCheck);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================
    // PROCESAR HORARIO CLASE
    // =========================================
    private void procesarHorarioClase(
        Connection conexion,
        int matricula,
        java.sql.Date fechaActual,
        Time horaActual
    ) {
        System.out.println("ENTRANDO A PROFESOR");
        try {
            String sql =
                "SELECT id_horarioClase, hora_inicio, hora_fin "
              + "FROM Horario_Clase "
              + "WHERE matricula = ?";

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setInt(1, matricula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("HORARIO CLASE ENCONTRADO");

                int idHorario = rs.getInt("id_horarioClase");
                LocalTime actual = horaActual.toLocalTime();
                LocalTime inicio = rs.getTime("hora_inicio").toLocalTime();
                LocalTime fin = rs.getTime("hora_fin").toLocalTime();

                System.out.println("Hora actual: " + actual);
                System.out.println("Hora inicio: " + inicio);
                System.out.println("Hora fin: " + fin);

                if (!actual.isBefore(inicio.minusMinutes(15))
                        && !actual.isAfter(inicio.plusMinutes(15))) {
                    System.out.println("ENTRADA A TIEMPO");
                    registroES.registrarEntradaClase(
                            conexion, matricula, fechaActual, horaActual, idHorario, false);

                } else if (actual.isAfter(inicio.plusMinutes(15))
                        && !actual.isAfter(inicio.plusMinutes(60))) {
                    System.out.println("RETARDO");
                    registroES.registrarEntradaClase(
                            conexion, matricula, fechaActual, horaActual, idHorario, true);

                } else if (!actual.isBefore(fin.minusMinutes(15))
                        && !actual.isAfter(fin.plusMinutes(5))) {
                    System.out.println("SALIDA");
                    registroES.registrarSalidaClase(
                            conexion, matricula, fechaActual, horaActual, idHorario);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================
    // PROCESAR HORARIO ADMINISTRADOR
    // =========================================
    private void procesarHorarioAdministrador(
        Connection conexion,
        int matricula,
        java.sql.Date fechaActual,
        Time horaActual
    ) {
        System.out.println("ENTRANDO A ADMIN");
        try {
            String sql =
                "SELECT id_horarioAdministrador, hora_inicio, hora_fin "
              + "FROM Horario_Administrador "
              + "WHERE matricula = ?";

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setInt(1, matricula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println("HORARIO ADMIN ENCONTRADO");

                int idHorario = rs.getInt("id_horarioAdministrador");
                LocalTime actual = horaActual.toLocalTime();
                LocalTime inicio = rs.getTime("hora_inicio").toLocalTime();
                LocalTime fin = rs.getTime("hora_fin").toLocalTime();

                System.out.println("Hora actual: " + actual);
                System.out.println("Hora inicio: " + inicio);
                System.out.println("Hora fin: " + fin);

                if (!actual.isBefore(inicio.minusMinutes(15))
                        && !actual.isAfter(inicio.plusMinutes(15))) {
                    System.out.println("ENTRADA A TIEMPO");
                    registroES.registrarEntradaAdministrador(
                            conexion, matricula, fechaActual, horaActual, idHorario, false);

                } else if (actual.isAfter(inicio.plusMinutes(15))
                        && !actual.isAfter(inicio.plusMinutes(60))) {
                    System.out.println("RETARDO");
                    registroES.registrarEntradaAdministrador(
                            conexion, matricula, fechaActual, horaActual, idHorario, true);

                } else if (!actual.isBefore(fin.minusMinutes(15))
                        && !actual.isAfter(fin.plusMinutes(5))) {
                    System.out.println("SALIDA");
                    registroES.registrarSalidaAdministrador(
                            conexion, matricula, fechaActual, horaActual, idHorario);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}