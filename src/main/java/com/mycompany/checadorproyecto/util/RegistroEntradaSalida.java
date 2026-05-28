/* TODA ESTA LÓGICA SE PASO A ADMIN


package com.mycompany.checadorproyecto.util;

import java.sql.*;

public class RegistroEntradaSalida {

    private int idChecador;

    public RegistroEntradaSalida(int idChecador) {
        this.idChecador = idChecador;
    }

    // =========================================
    // ENTRADA CLASE
    // =========================================
    public void registrarEntradaClase(
        Connection conexion,
        int matricula,
        java.sql.Date fechaActual,
        Time horaActual,
        int idHorario,
        boolean retardo
    ) {
        try {
            String verificar =
                "SELECT id_checadas "
              + "FROM Checadas "
              + "WHERE matricula = ? "
              + "AND fecha = ? "
              + "AND id_horarioClase = ? "
              + "AND hora_salida IS NULL";

            PreparedStatement psVerificar =
                    conexion.prepareStatement(verificar);

            psVerificar.setInt(1, matricula);
            psVerificar.setDate(2, fechaActual);
            psVerificar.setInt(3, idHorario);

            ResultSet rs = psVerificar.executeQuery();

            if (rs.next()) {
                return;
            }

            String sql =
                "INSERT INTO Checadas "
              + "(fecha, matricula, id_checador, "
              + "hora_entrada, hora_salida, status, "
              + "id_horarioClase, id_horarioAdmin) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setDate(1, fechaActual);
            ps.setInt(2, matricula);
            ps.setInt(3, idChecador);
            ps.setTime(4, horaActual);
            ps.setNull(5, Types.TIME);
            ps.setString(6, retardo ? "retardo" : "a tiempo");
            ps.setInt(7, idHorario);
            ps.setNull(8, Types.INTEGER);

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================
    // SALIDA CLASE
    // =========================================
    public void registrarSalidaClase(
        Connection conexion,
        int matricula,
        java.sql.Date fechaActual,
        Time horaActual,
        int idHorario
    ) {
        try {
            String sql =
                "SELECT id_checadas, status "
              + "FROM Checadas "
              + "WHERE matricula = ? "
              + "AND fecha = ? "
              + "AND id_horarioClase = ? "
              + "AND hora_salida IS NULL";

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setInt(1, matricula);
            ps.setDate(2, fechaActual);
            ps.setInt(3, idHorario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idChecada = rs.getInt("id_checadas");
                String statusAnterior = rs.getString("status");
                String nuevoStatus = statusAnterior.equals("a tiempo")
                        ? "completado" : "incompleto";

                String update =
                    "UPDATE Checadas "
                  + "SET hora_salida = ?, status = ? "
                  + "WHERE id_checadas = ?";

                PreparedStatement psUpdate =
                        conexion.prepareStatement(update);

                psUpdate.setTime(1, horaActual);
                psUpdate.setString(2, nuevoStatus);
                psUpdate.setInt(3, idChecada);
                psUpdate.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================
    // ENTRADA ADMINISTRADOR
    // =========================================
    public void registrarEntradaAdministrador(
        Connection conexion,
        int matricula,
        java.sql.Date fechaActual,
        Time horaActual,
        int idHorario,
        boolean retardo
    ) {
        try {
            String verificar =
                "SELECT id_checadas "
              + "FROM Checadas "
              + "WHERE matricula = ? "
              + "AND fecha = ? "
              + "AND id_horarioAdmin = ? "
              + "AND hora_salida IS NULL";

            PreparedStatement psVerificar =
                    conexion.prepareStatement(verificar);

            psVerificar.setInt(1, matricula);
            psVerificar.setDate(2, fechaActual);
            psVerificar.setInt(3, idHorario);

            ResultSet rs = psVerificar.executeQuery();

            if (rs.next()) {
                return;
            }

            String sql =
                "INSERT INTO Checadas "
              + "(fecha, matricula, id_checador, "
              + "hora_entrada, hora_salida, status, "
              + "id_horarioClase, id_horarioAdmin) "
              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setDate(1, fechaActual);
            ps.setInt(2, matricula);
            ps.setInt(3, idChecador);
            ps.setTime(4, horaActual);
            ps.setNull(5, Types.TIME);
            ps.setString(6, retardo ? "retardo" : "a tiempo");
            ps.setNull(7, Types.INTEGER);
            ps.setInt(8, idHorario);

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =========================================
    // SALIDA ADMINISTRADOR
    // =========================================
    public void registrarSalidaAdministrador(
        Connection conexion,
        int matricula,
        java.sql.Date fechaActual,
        Time horaActual,
        int idHorario
    ) {
        try {
            String sql =
                "SELECT id_checadas, status "
              + "FROM Checadas "
              + "WHERE matricula = ? "
              + "AND fecha = ? "
              + "AND id_horarioAdmin = ? "
              + "AND hora_salida IS NULL";

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setInt(1, matricula);
            ps.setDate(2, fechaActual);
            ps.setInt(3, idHorario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idChecada = rs.getInt("id_checadas");
                String statusAnterior = rs.getString("status");
                String nuevoStatus = statusAnterior.equals("a tiempo")
                        ? "completado" : "incompleto";

                String update =
                    "UPDATE Checadas "
                  + "SET hora_salida = ?, status = ? "
                  + "WHERE id_checadas = ?";

                PreparedStatement psUpdate =
                        conexion.prepareStatement(update);

                psUpdate.setTime(1, horaActual);
                psUpdate.setString(2, nuevoStatus);
                psUpdate.setInt(3, idChecada);
                psUpdate.executeUpdate();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}*/