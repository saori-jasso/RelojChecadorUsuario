package com.mycompany.checadorproyecto.util;

import com.mycompany.checadorproyecto.bd.ConexionBD;
import java.awt.Color;
import java.sql.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.*;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.*;
import com.digitalpersona.onetouch.verification.*;

public class LectorHuella {

    private DPFPCapture lector =
            DPFPGlobal.getCaptureFactory().createCapture();

    private DPFPVerification verificador =
            DPFPGlobal.getVerificationFactory().createVerification();

    private DPFPFeatureExtraction extractor =
            DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();

    private boolean procesandoHuella = false;

    private RegistroChecada registroChecada;
    private InterfazChecador interfaz;

    public LectorHuella(RegistroChecada registroChecada, InterfazChecador interfaz) {
        this.registroChecada = registroChecada;
        this.interfaz = interfaz;
    }

    // =========================================
    // INICIAR LECTOR
    // =========================================
    public void iniciarLector(
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        lector.addReaderStatusListener(
            new DPFPReaderStatusAdapter() {
                @Override
                public void readerConnected(DPFPReaderStatusEvent e) {
                    System.out.println("Sensor conectado");
                }
                @Override
                public void readerDisconnected(DPFPReaderStatusEvent e) {
                    System.out.println("Sensor desconectado");
                }
            }
        );

        lector.addDataListener(
            new DPFPDataAdapter() {
                @Override
                public void dataAcquired(DPFPDataEvent e) {
                    procesarHuella(e.getSample(), txtID, txtFecha, txtHoraRegistrada, lblCheck);
                }
            }
        );

        lector.startCapture();
    }

    // =========================================
    // PROCESAR HUELLA
    // =========================================
    private void procesarHuella(
        DPFPSample sample,
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        if (procesandoHuella) {
            return;
        }

        procesandoHuella = true;

        try {
            DPFPFeatureSet features =
                    extractor.createFeatureSet(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

            if (features == null) {
                JOptionPane.showMessageDialog(null, "No se pudo leer correctamente la huella");
                procesandoHuella = false;
                return;
            }

            boolean coincidencia =
                    buscarHuellaEnBD(features, txtID, txtFecha, txtHoraRegistrada, lblCheck);

            if (!coincidencia) {
                lblCheck.setText("DENEGADO");
                lblCheck.setForeground(Color.RED);
                interfaz.limpiarCamposDespues(txtID, txtFecha, txtHoraRegistrada, lblCheck);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Timer desbloqueo = new Timer(3000, e -> {
                procesandoHuella = false;
            });
            desbloqueo.setRepeats(false);
            desbloqueo.start();
        }
    }

    // =========================================
    // BUSCAR HUELLA EN BD
    // =========================================
    private boolean buscarHuellaEnBD(
        DPFPFeatureSet features,
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        String sql = "SELECT matricula, huella FROM Empleados";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int matricula = rs.getInt("matricula");
                byte[] huellaBD = rs.getBytes("huella");

                DPFPTemplate template =
                        DPFPGlobal.getTemplateFactory().createTemplate(huellaBD);

                DPFPVerificationResult resultado =
                        verificador.verify(features, template);

                if (resultado.isVerified()) {
                    registroChecada.registrarChecada(conexion, matricula);
                    interfaz.actualizarInterfazCorrecta(
                            matricula, txtID, txtFecha, txtHoraRegistrada, lblCheck);
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }
}