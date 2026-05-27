package com.mycompany.checadorproyecto.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class RelojDigital {

    private int idChecador = 3; // 1=UAE1, 2=CC, 3=CNT
    private Timer timer;

    private InterfazChecador interfaz;
    private RegistroChecada registroChecada;
    private LectorHuella lectorHuella;

    public RelojDigital() {
        this.interfaz = new InterfazChecador();
        this.registroChecada = new RegistroChecada(idChecador);
        this.lectorHuella = new LectorHuella(registroChecada, interfaz);
    }

    // =========================================
    // INICIAR RELOJ
    // =========================================
    public void iniciar(
        JLabel lblHora,
        JTextField txtFecha,
        JTextField txtHoraRegistrada
    ) {
        timer = new Timer(1000, e -> {
            Date fechaActual = new Date();
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
            lblHora.setText(formatoHora.format(fechaActual));
        });
        timer.start();
    }

    // =========================================
    // INICIAR LECTOR DE HUELLA
    // =========================================
    public void iniciarLector(
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        lectorHuella.iniciarLector(txtID, txtFecha, txtHoraRegistrada, lblCheck);
    }

    // =========================================
    // REGISTRAR POR MATRÍCULA
    // =========================================
    public void registrarPorMatricula(
        String matriculaTexto,
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        registroChecada.registrarPorMatricula(
                matriculaTexto, txtID, txtFecha, txtHoraRegistrada, lblCheck, interfaz);
    }
}