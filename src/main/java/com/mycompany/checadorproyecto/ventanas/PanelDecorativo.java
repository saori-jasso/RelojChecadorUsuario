/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadorproyecto.ventanas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Andrea
 */
public class PanelDecorativo extends JPanel{
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //SUAVIZado
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        //FONDO PRINCIPAL
        g2.setColor(new Color(245, 245, 245));
        g2.fillRect(0, 0, getWidth(), getHeight());
        //DECORACIONES AMARILLAS
        g2.setColor(new Color(255, 214, 89));
        g2.fillOval(-80, -80, 200, 200);// Superior izquierda
        g2.fillOval(630, -80, 200, 200);// Superior derecha
        g2.fillOval(-80, 440, 200, 200);// Inferior izquierda
        g2.fillOval(630, 440, 200, 200);// Inferior derecha
    }
}
