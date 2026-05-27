package com.mycompany.checadorproyecto.util;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class InterfazChecador {

    public void actualizarInterfazCorrecta(
        int matricula,
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        txtID.setText(String.valueOf(matricula));
        txtFecha.setText(formatoFecha.format(fechaActual));
        txtHoraRegistrada.setText(formatoHora.format(fechaActual));

        lblCheck.setText("A");
        lblCheck.setForeground(new Color(0, 150, 0));

        limpiarCamposDespues(txtID, txtFecha, txtHoraRegistrada, lblCheck);
    }

    public void limpiarCamposDespues(
        JTextField txtID,
        JTextField txtFecha,
        JTextField txtHoraRegistrada,
        JLabel lblCheck
    ) {
        Timer limpiar = new Timer(15000, e -> {
            txtID.setText("");
            txtFecha.setText("");
            txtHoraRegistrada.setText("");
            lblCheck.setText("");
            lblCheck.setForeground(new Color(132, 195, 65));
        });
        limpiar.setRepeats(false);
        limpiar.start();
    }
}