package com.mycompany.checadorproyecto.util;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;

public class RegistroChecada {

    private int idChecador;

    public RegistroChecada(int idChecador) {
        this.idChecador = idChecador;
    }

    // =========================================
    // REGISTRAR POR MATRÍCULA (ÚNICO MÉTODO AQUÍ)
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

        new Thread(() -> {
            // LA IP DE TU COMPUTADORA SERVIDORA
            String ipServidor = "192.168.0.182"; // <-- Cambiar por la del Access Point
            int puertoServidor = 5000;

            try (java.net.Socket socket = new java.net.Socket(ipServidor, puertoServidor);
                 java.io.DataOutputStream salida = new java.io.DataOutputStream(socket.getOutputStream());
                 java.io.DataInputStream entrada = new java.io.DataInputStream(socket.getInputStream())) {

                salida.writeUTF(matriculaTexto); // Enviamos matrícula al servidor
                String respuesta = entrada.readUTF(); // Esperamos que el servidor la procese y nos diga qué pasó

                if (respuesta.equals("EXITO")) {
                    int matricula = Integer.parseInt(matriculaTexto.trim());
                    interfaz.actualizarInterfazCorrecta(matricula, txtID, txtFecha, txtHoraRegistrada, lblCheck);
                } else {
                    lblCheck.setText("DENEGADO");
                    lblCheck.setForeground(Color.RED);
                    interfaz.limpiarCamposDespues(txtID, txtFecha, txtHoraRegistrada, lblCheck);
                    JOptionPane.showMessageDialog(null, respuesta);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error de Red al conectar con el Servidor.");
            }
        }).start();
    }
}