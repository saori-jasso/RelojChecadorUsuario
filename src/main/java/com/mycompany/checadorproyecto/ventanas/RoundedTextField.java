/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadorproyecto.ventanas;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author Andrea
 */
public class RoundedTextField extends JTextField {
    //constructor
    public RoundedTextField() {
        setOpaque(false);// Fondo transparente
        setHorizontalAlignment(JTextField.CENTER);// Centrar texto
        setFont(new Font("Arial", Font.PLAIN, 16));//Fuente
        setBackground(new Color(210, 210, 210));//cambiar el color fondo
        setBorder(new EmptyBorder(5, 10, 5, 10));//Espaciado interno
    }
    //DIBUJAR COMPONENTE
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Suavizar bordes para que se vean redondeados
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        //dibujar el rectángulo redondeado
        g2.setColor(getBackground());
        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                30,
                30
        );
        super.paintComponent(g2);//dibujar texto
        g2.dispose();
    }

    //DIBUJAR BORDE
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setColor(new Color(180, 180, 180));// Color borde

       // BORDE REDONDEADO
        g2.drawRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                30,
                30
        );
        g2.dispose();
    }
}