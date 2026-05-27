/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadorproyecto.util;
import com.mycompany.checadorproyecto.bd.ConexionBD;//Para la conexion con la BD
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JTextField;
//JDK y RTE para DigitalMy Persona
import com.digitalpersona.onetouch.*;//para conectar con las librerías de digitalmypersona
import com.digitalpersona.onetouch.capture.*;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.*;
import com.digitalpersona.onetouch.verification.*;
import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.time.LocalTime;
/**
 *
 * @author danie
 */
public class RelojDigital {
    //Variables globales
    private int idChecador=3;//1 para UAE1, 2 para CC y 3 para CNT CAMBIAR ESTO POR LAPTOP
    private Timer timer; //timer que actualiza la hora automáticamengte 
    private DPFPCapture lector =//VARIABLE QUE LEERÁ LA HUELLA
        DPFPGlobal.getCaptureFactory().createCapture();
    private boolean procesandoHuella = false;
    
    //Variables de huella para detectarla, verificarla, y guardarla temporalmente
    private DPFPVerification verificador =
        DPFPGlobal.getVerificationFactory().createVerification();

    private DPFPFeatureExtraction extractor =
        DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
   
    //Lector de huella
    public void iniciarLector( JTextField txtID,JTextField txtFecha,JTextField txtHoraRegistrada,
    JLabel lblCheck){
        lector.addReaderStatusListener(
            new DPFPReaderStatusAdapter() {
                @Override
                public void readerConnected(
                        DPFPReaderStatusEvent e
                ) {
                    System.out.println(
                            "Sensor conectado"
                    );
                }
                @Override
                public void readerDisconnected(
                        DPFPReaderStatusEvent e
                ) {
                    System.out.println(
                            "Sensor desconectado"
                    );
                }
            }
        );
        //EVENTO CUANDO SE LEE UNA HUELLA
        lector.addDataListener(
            new DPFPDataAdapter() {
                @Override
                public void dataAcquired(
                        DPFPDataEvent e
                ) {
                    procesarHuella( e.getSample(),txtID,txtFecha,txtHoraRegistrada,lblCheck);
                }
            }
        );
        lector.startCapture();
    }
    private void procesarHuella(
        DPFPSample sample,
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
) {

    if(procesandoHuella) {
        return;
    }

    procesandoHuella = true;

    try {

        DPFPFeatureSet features =
                extractor.createFeatureSet(
                        sample,
                        DPFPDataPurpose.DATA_PURPOSE_VERIFICATION
                );

        if(features == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "No se pudo leer correctamente la huella"
            );

            procesandoHuella = false;

            return;
        }

        boolean coincidencia =
                buscarHuellaEnBD(
                        features,
                        txtID,
                        txtFecha,
                        txtHoraRegistrada,
                        lblCheck
                );

        if(!coincidencia) {

            lblCheck.setText("DENEGADO");

            lblCheck.setForeground(Color.RED);

            limpiarCamposDespues(
                    txtID,
                    txtFecha,
                    txtHoraRegistrada,
                    lblCheck
            );
        }

    } catch(Exception ex) {

        ex.printStackTrace();

    } finally {

        Timer desbloqueo = new Timer(3000, e -> {
            procesandoHuella = false;
        });

        desbloqueo.setRepeats(false);

        desbloqueo.start();
    }
}
    private boolean buscarHuellaEnBD(DPFPFeatureSet features,JTextField txtID,JTextField txtFecha,
    JTextField txtHoraRegistrada,JLabel lblCheck) {
        String sql = "SELECT matricula, huella FROM Empleados";
   
        try(Connection conexion = ConexionBD.conectar(); PreparedStatement ps = conexion.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();) {
            while(rs.next()) {
                int matricula =
                        rs.getInt("matricula");
                byte[] huellaBD =
                        rs.getBytes("huella");
                DPFPTemplate template =
                        DPFPGlobal
                        .getTemplateFactory()
                        .createTemplate(
                                huellaBD
                        );
                DPFPVerificationResult resultado =
                        verificador.verify(
                                features,
                                template
                        );

                if(resultado.isVerified()) {
                    registrarChecada(conexion,matricula);
                    actualizarInterfazCorrecta(matricula,txtID,txtFecha,txtHoraRegistrada,lblCheck);
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    private void registrarChecada(Connection conexion,int matricula) {
        /*String sql =
            "INSERT INTO Checadas "
          + "(fecha, matricula, id_checador, hora_entrada, hora_salida, status) "
          + "VALUES (?, ?, ?, ?, ?, ?)";
        try (
            PreparedStatement ps =
                    conexion.prepareStatement(sql)
        ) {
            java.sql.Date fecha =
                    new java.sql.Date(System.currentTimeMillis());
            java.sql.Time hora =
                    new java.sql.Time(System.currentTimeMillis());
            ps.setDate(1, fecha);
            ps.setInt(2, matricula);
            ps.setInt(3, idChecador);
            ps.setTime(4, hora);
            ps.setNull(5, Types.TIME);
            ps.setString(6, "incompleto");
            ps.executeUpdate();
        } catch(Exception ex) {
            ex.printStackTrace();
        }*/
        
    try {

        // Hora y fecha actual
        Timestamp fechaHoraActual = new Timestamp(System.currentTimeMillis());

        java.sql.Date fechaActual =
                new java.sql.Date(System.currentTimeMillis());

        Time horaActual =
                new Time(System.currentTimeMillis());

        // =========================================
        // OBTENER PUESTO DEL EMPLEADO
        // =========================================
        String sqlEmpleado =
                "SELECT puesto FROM Empleados WHERE matricula = ?";

        PreparedStatement psEmpleado =
                conexion.prepareStatement(sqlEmpleado);

        psEmpleado.setInt(1, matricula);

        ResultSet rsEmpleado =
                psEmpleado.executeQuery();

        if(!rsEmpleado.next()) {
            return;
        }

        String puesto =
                rsEmpleado.getString("puesto").toLowerCase();
        System.out.println("PUESTO: " + puesto);

        // =========================================
        // SI ES PROFESOR
        // =========================================
        if(
            puesto.contains("profesor")
            || puesto.contains("ambos")
        ) {

            procesarHorarioClase(
                    conexion,
                    matricula,
                    fechaActual,
                    horaActual
            );
        }

        // =========================================
        // SI ES ADMINISTRADOR
        // =========================================
        if(
            puesto.contains("administrativo")
            || puesto.contains("ambos")
        ) {

            procesarHorarioAdministrador(
                    conexion,
                    matricula,
                    fechaActual,
                    horaActual
            );
        }

    } catch(Exception ex) {
        ex.printStackTrace();
    }
    }
    
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

        ResultSet rs =
                ps.executeQuery();

        while(rs.next()) {
            System.out.println("HORARIO CLASE ENCONTRADO");
            int idHorario =
                    rs.getInt("id_horarioClase");

            Time horaInicio =
                    rs.getTime("hora_inicio");

            Time horaFin =
                    rs.getTime("hora_fin");

            /*long actual =
                    horaActual.getTime();

            long inicio =
                    horaInicio.getTime();

            long fin =
                    horaFin.getTime();

            long quinceMin =
                    15 * 60 * 1000;

            long cincoMin =
                    5 * 60 * 1000;

            long sesentaMin =
                    60 * 60 * 1000;
                        */
            LocalTime actual =
        horaActual.toLocalTime();

LocalTime inicio =
        horaInicio.toLocalTime();

LocalTime fin =
        horaFin.toLocalTime();
System.out.println("Hora actual: " + actual);
System.out.println("Hora inicio: " + inicio);
System.out.println("Hora fin: " + fin);
            // =====================================
            // ENTRADA A TIEMPO
            // =====================================
            if(
                !actual.isBefore(inicio.minusMinutes(15))
    &&
    !actual.isAfter(inicio.plusMinutes(15))
            ) {
System.out.println("ENTRADA A TIEMPO");
                registrarEntradaClase(
                        conexion,
                        matricula,
                        fechaActual,
                        horaActual,
                        idHorario,
                        false
                );
            }

            // =====================================
            // ENTRADA CON RETARDO
            // =====================================
            else if(
                actual.isAfter(inicio.plusMinutes(15))
    &&
    !actual.isAfter(inicio.plusMinutes(60))
            ) {
System.out.println("RETARDO");
                registrarEntradaClase(
                        conexion,
                        matricula,
                        fechaActual,
                        horaActual,
                        idHorario,
                        true
                );
            }

            // =====================================
            // SALIDA
            // =====================================
            else if(
                !actual.isBefore(fin.minusMinutes(15))
    &&
    !actual.isAfter(fin.plusMinutes(5))
            ) {
System.out.println("SALIDA");
                registrarSalidaClase(
                        conexion,
                        matricula,
                        fechaActual,
                        horaActual,
                        idHorario
                );
            }
        }

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
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

        ResultSet rs =
                ps.executeQuery();

        while(rs.next()) {
System.out.println("HORARIO ADMIN ENCONTRADO");
            int idHorario =
                    rs.getInt("id_horarioAdministrador");

            Time horaInicio =
                    rs.getTime("hora_inicio");

            Time horaFin =
                    rs.getTime("hora_fin");

            /*long actual =
                    horaActual.getTime();

            long inicio =
                    horaInicio.getTime();

            long fin =
                    horaFin.getTime();

            long quinceMin =
                    15 * 60 * 1000;

            long cincoMin =
                    5 * 60 * 1000;

            long sesentaMin =
                    60 * 60 * 1000;
                    */
            LocalTime actual =
        horaActual.toLocalTime();

LocalTime inicio =
        horaInicio.toLocalTime();

LocalTime fin =
        horaFin.toLocalTime();
System.out.println("Hora actual: " + actual);
System.out.println("Hora inicio: " + inicio);
System.out.println("Hora fin: " + fin);
            // =====================================
            // ENTRADA A TIEMPO
            // =====================================
            if(
                !actual.isBefore(inicio.minusMinutes(15))
    &&
    !actual.isAfter(inicio.plusMinutes(15))
            ) {
                System.out.println("ENTRADA A TIEMPO");
                registrarEntradaAdministrador(
                        conexion,
                        matricula,
                        fechaActual,
                        horaActual,
                        idHorario,
                        false
                );
            }

            // =====================================
            // ENTRADA RETARDO
            // =====================================
            else if(
                actual.isAfter(inicio.plusMinutes(15))
    &&
    !actual.isAfter(inicio.plusMinutes(60))
            ) {
                System.out.println("RETARDO");
                registrarEntradaAdministrador(
                        conexion,
                        matricula,
                        fechaActual,
                        horaActual,
                        idHorario,
                        true
                );
            }

            // =====================================
            // SALIDA
            // =====================================
            else if(!actual.isBefore(fin.minusMinutes(15))
    &&
    !actual.isAfter(fin.plusMinutes(5))
            ) {
                System.out.println("SALIDA");
                registrarSalidaAdministrador(
                        conexion,
                        matricula,
                        fechaActual,
                        horaActual,
                        idHorario
                );
            }
        }

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
    private void registrarEntradaAdministrador(
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

        ResultSet rs =
                psVerificar.executeQuery();

        if(rs.next()) {
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

        if(retardo) {
            ps.setString(6, "retardo");
        } else {
            ps.setString(6, "a tiempo");
        }

        ps.setNull(7, Types.INTEGER);

        ps.setInt(8, idHorario);

        ps.executeUpdate();

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
    private void registrarSalidaAdministrador(
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

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            int idChecada =
                    rs.getInt("id_checadas");

            String statusAnterior =
                    rs.getString("status");

            String nuevoStatus;

            if(statusAnterior.equals("a tiempo")) {
                nuevoStatus = "completado";
            } else {
                nuevoStatus = "incompleto";
            }

            String update =
                    "UPDATE Checadas "
                  + "SET hora_salida = ?, "
                  + "status = ? "
                  + "WHERE id_checadas = ?";

            PreparedStatement psUpdate =
                    conexion.prepareStatement(update);

            psUpdate.setTime(1, horaActual);

            psUpdate.setString(2, nuevoStatus);

            psUpdate.setInt(3, idChecada);

            psUpdate.executeUpdate();
        }

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
    
    private void registrarEntradaClase(
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

        ResultSet rs =
                psVerificar.executeQuery();

        if(rs.next()) {
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

        if(retardo) {
            ps.setString(6, "retardo");
        } else {
            ps.setString(6, "a tiempo");
        }

        ps.setInt(7, idHorario);

        ps.setNull(8, Types.INTEGER);

        ps.executeUpdate();

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
    private void registrarSalidaClase(
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

        ResultSet rs =
                ps.executeQuery();

        if(rs.next()) {

            int idChecada =
                    rs.getInt("id_checadas");

            String statusAnterior =
                    rs.getString("status");

            String nuevoStatus;

            if(statusAnterior.equals("a tiempo")) {
                nuevoStatus = "completado";
            } else {
                nuevoStatus = "incompleto";
            }

            String update =
                    "UPDATE Checadas "
                  + "SET hora_salida = ?, "
                  + "status = ? "
                  + "WHERE id_checadas = ?";

            PreparedStatement psUpdate =
                    conexion.prepareStatement(update);

            psUpdate.setTime(1, horaActual);
            psUpdate.setString(2, nuevoStatus);
            psUpdate.setInt(3, idChecada);

            psUpdate.executeUpdate();
        }

    } catch(Exception ex) {
        ex.printStackTrace();
    }
}
    
    private void actualizarInterfazCorrecta(int matricula,JTextField txtID,JTextField txtFecha,JTextField txtHoraRegistrada,
    JLabel lblCheck) {
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        txtID.setText(
                String.valueOf(matricula)
        );
        txtFecha.setText(
                formatoFecha.format(fechaActual)
        );
        txtHoraRegistrada.setText(
                formatoHora.format(fechaActual)
        );
        lblCheck.setText("A");
        lblCheck.setForeground(
                new Color(0, 150, 0)
        );
        limpiarCamposDespues(txtID,txtFecha,txtHoraRegistrada,lblCheck);
    }
    private void limpiarCamposDespues(JTextField txtID,JTextField txtFecha,JTextField txtHoraRegistrada,JLabel lblCheck){
        Timer limpiar = new Timer(15000, e ->{
            txtID.setText("");
            txtFecha.setText("");
            txtHoraRegistrada.setText("");
            lblCheck.setText("");
            lblCheck.setForeground(
                    new Color(132, 195, 65)
            );
        });
        limpiar.setRepeats(false);
        limpiar.start();
    }
    public void iniciar( //método para iniciar el reloj
            JLabel lblHora,
            JTextField txtFecha,
            JTextField txtHoraRegistrada
    ) {

        timer = new Timer(1000, e -> {//para ejecutar el codigo cada seg
            Date fechaActual = new Date();//obtener fecha y hora actual 
            //formato de la hora
            SimpleDateFormat formatoHora =
                    new SimpleDateFormat("HH:mm");
            //formato de la fecha
            SimpleDateFormat formatoFecha =
                    new SimpleDateFormat("dd/MM/yyyy");
            lblHora.setText(//actualizar el reloj 
                    formatoHora.format(fechaActual)
            );
        });
        timer.start();//iniciar el timer 
    }
}
