/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.checadorproyecto.main;

import com.mycompany.checadorproyecto.ventanas.VentanaChecador;//se debe importar clase de otro paquete
import javax.swing.SwingUtilities;
/**
 *
 * @author danie
 */
public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {//ejecuta la ventana en el hilo de swing 
            new VentanaChecador().setVisible(true);
        });
        
        

    }
}
