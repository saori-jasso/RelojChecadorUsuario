/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadorproyecto.ventanas;

import com.mycompany.checadorproyecto.util.RelojDigital;//importar a la clase del reloj
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author danie
 */
public class VentanaChecador extends JFrame {

    private JLabel lblHora;//reloj principal 
    
    //campos de texto 
    private RoundedTextField txtID;
    private RoundedTextField txtFecha;
    private RoundedTextField txtHoraRegistrada;
    private JLabel lblCheck;

    public VentanaChecador() {//el contructor

        configurarVentana();
        crearComponentes();
        iniciarReloj();

    }

    private void configurarVentana() {//configuración de la ventana 

        setTitle("Checador");
        setSize(750, 550);
        setLocationRelativeTo(null);//centrarla 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//cerrar programa al cerrar la ventana
        setResizable(false);//evitar cambios de tamaño
        setLayout(null);

    }

    private void crearComponentes() {//creación de componentes 

        PanelDecorativo panel = new PanelDecorativo();
        panel.setBounds(0, 0, 750, 550);//Tamaño actualizado 9na versión
        panel.setLayout(null);
        setContentPane(panel);//colocal el panel como cont. principal

        // RELOJ
        lblHora = new JLabel("00:00");
        lblHora.setHorizontalAlignment(/*centrar texto*/SwingConstants.CENTER);
        lblHora.setFont(//fuente y tamaño que escogimos
                new Font("Arial", Font.BOLD, 42)
        );

        lblHora.setBounds(275, 40, 200, 50);//posición

        panel.add(lblHora);

        // LABELS de la izquierda 
        JLabel lblID = crearLabel("ID");
        lblID.setBounds(250, 140, 100, 30);

        JLabel lblFecha = crearLabel("FECHA");
        lblFecha.setBounds(250, 190, 100, 30);

        JLabel lblHoraReg =
                crearLabel("HORA REGISTRADA");

        lblHoraReg.setBounds(140, 240, 220, 30);

        panel.add(lblID);
        panel.add(lblFecha);
        panel.add(lblHoraReg);

        // TEXTFIELDS de la derecha 
        txtID = new RoundedTextField();
        txtID.setText("");//Empieza vacío
        txtID.setEditable(false);
        txtID.setBounds(370, 140, 110, 35);

        panel.add(txtID);

        txtFecha = new RoundedTextField();
        txtFecha.setEditable(false);//para que no escriban
        txtFecha.setBounds(370, 190, 110, 35);

        panel.add(txtFecha);

        txtHoraRegistrada = new RoundedTextField();
        txtHoraRegistrada.setEditable(false);
        txtHoraRegistrada.setBounds(370, 240, 110, 35);

        panel.add(txtHoraRegistrada);

        JLabel lblVerificacion = new JLabel(//texto de l averificación por huella
                "<html>VERIFICACIÓN<br>DACTILAR</html>"
        );

        lblVerificacion.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        lblVerificacion.setBounds(180, 300, 180, 50);

        panel.add(lblVerificacion);
        //icono 
        lblCheck = new JLabel("");
        lblCheck.setFont(
                new Font("Arial", Font.BOLD, 46)
        );

        lblCheck.setForeground(
                new Color(132, 195, 65)
        );

        lblCheck.setBounds(385, 295, 60, 60);

        panel.add(lblCheck);

    }

    private JLabel crearLabel(String texto) {//método para crear labels sin tener que repetir cosas

        JLabel label = new JLabel(texto);
        
        label.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        label.setForeground(
                new Color(50, 50, 50)
        );
        
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;

    }

    private void iniciarReloj() {//crear e iniciar el reloj

        RelojDigital reloj = new RelojDigital();
        reloj.iniciar( lblHora,txtFecha,txtHoraRegistrada);
        reloj.iniciarLector(txtID,txtFecha,txtHoraRegistrada,lblCheck);//Prueba 1 con código, captura y BD
    }
}