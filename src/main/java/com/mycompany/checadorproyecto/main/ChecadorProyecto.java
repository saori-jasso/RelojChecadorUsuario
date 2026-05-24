package com.mycompany.checadorproyecto.main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import com.mycompany.checadorproyecto.ventanas.PanelDecorativo;
import com.mycompany.checadorproyecto.ventanas.RoundedTextField;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Andrea
 */

public class ChecadorProyecto extends JFrame {
    //COMPONENTES GLOBALES
    private JLabel lblHora;// Label del reloj principal

    private RoundedTextField txtID;// labels  (en este caso RoundedTextField )que se rellanaran automáticamente 
    private RoundedTextField txtFecha;
    private RoundedTextField txtHoraRegistrada;
    
    private Timer timer;// Timer para actualizar el reloj

    //CONSTRUCTOR
    public ChecadorProyecto() {
        configurarVentana();// Configuración general ventana
        crearComponentes();// crea y agrega todo lo visual a la ventanaa
        iniciarReloj();//Inicia el reloj
    }
    //CONFIGURACIÓN DE VENTANA
    private void configurarVentana() {
        setTitle("Checador");//título
        setSize(750, 550);//tamaño
        setLocationRelativeTo(null);//centrar la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);//desactivar redimensionamiento por el usuario, tamaño fijo
        setLayout(null);//layout libre
    }
    //CREACIÓN COMPONENTES
    private void crearComponentes() {
        //PANEL DE FONDO
        PanelDecorativo panel = new PanelDecorativo();
        panel.setBounds(0, 0, 640, 380);//Tamaño
        panel.setLayout(null);//layout libre
        setContentPane(panel);//establecer panel como fondo

        //RELOJ
        lblHora = new JLabel("00:00");//antes de actualizar
        lblHora.setHorizontalAlignment(SwingConstants.CENTER);//Centrar texto
        lblHora.setFont(new Font("Arial", Font.BOLD, 42));//le cambié la fuente para que estuviera en negritas
        lblHora.setBounds(220, 40, 200, 50);//posición
        panel.add(lblHora);//agregado al panel

        //LABELS IZQUIERDA LABELS PARA TITULOS
        JLabel lblID = crearLabel("ID");//label de ID con un método para emparejar a todos 
                                        //con el mismo formato y no hacer 4 líneas por label
        lblID.setBounds(210, 140, 120, 30);//posición
        JLabel lblFecha = crearLabel("FECHA");//label fecha
        lblFecha.setBounds(210, 190, 120, 30);//posición
        JLabel lblHoraReg = crearLabel("HORA REGISTRADA");//label de hora
        lblHoraReg.setBounds(120, 240, 200, 30);//posición
        panel.add(lblID);//agregado al panel
        panel.add(lblFecha);//agregado al panel
        panel.add(lblHoraReg);//agregado al panel
        
        //TEXTFIELDS CON DISEÑO DE LA DERECHA
            //Campo ID
        txtID = new RoundedTextField();
        txtID.setText("000000");//como default
        txtID.setEditable(false);//bloquear edición
        txtID.setBounds(340, 140, 110, 35);//Posición
        panel.add(txtID);//agregado al panel
            //FECHA
        txtFecha = new RoundedTextField();
        txtFecha.setText("DD/MM/YYYY");//como default
        txtFecha.setEditable(false);//bloquear edición
        txtFecha.setBounds(340, 190, 110, 35);//Posición
        panel.add(txtFecha);//agregado al panel
            //hora registrada
        txtHoraRegistrada = new RoundedTextField();
        txtHoraRegistrada.setText("00:00");//como default
        txtHoraRegistrada.setEditable(false);//bloquear edición
        txtHoraRegistrada.setBounds(340, 240, 110, 35);//Posición
        panel.add(txtHoraRegistrada);//agregado al panel
            //verificación dactilar que es 1 línea por palabra por si quieren checar cómo es 
        JLabel lblVerificacion = new JLabel(
                "<html>VERIFICACIÓN<br>DACTILAR</html>"//es escribirlo como si fuera un html y el salto es un br normal
        );
        lblVerificacion.setFont(new Font("Arial", Font.BOLD, 18));//le cambié la fuente para que estuviera en negritas
        lblVerificacion.setBounds(170, 285, 150, 50);//posición
        panel.add(lblVerificacion);//agregado al panel

            //cosa de imagen para verificación por ahora lo deje como Label aunce sería ImageIcon
        JLabel lblCheck = new JLabel("V");
        lblCheck.setFont(new Font("Arial", Font.BOLD, 46));//Todo esto se va a cambiar pq será un icono
        lblCheck.setForeground(new Color(132, 195, 65));
        lblCheck.setBounds(340, 280, 60, 60);
        panel.add(lblCheck);
    }
    //MÉTODO PARA LOS LABELS
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 18));//cambio de fuente y negritas
        label.setForeground(new Color(50, 50, 50));//cambio de color del texto
        return label;
    }
    //RELOJ AUTOMÁTICO
    private void iniciarReloj() {
        //Timer hace que se ejecute el código cada 1 segundo
        timer = new Timer(1000, e -> {
            Date fechaActual = new Date();//Obtener la fecha actual sistema
            //Agregarle el formato de hora
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
            //formato de fecha
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            
            //actualizar los textos
            lblHora.setText(//actualizar hora
                    formatoHora.format(fechaActual)
            );
            txtFecha.setText(//actualizar fecha
                    formatoFecha.format(fechaActual)
            );
            txtHoraRegistrada.setText(//ESTO SE VA A QUITAR PERO PUSE EL MISMO QUE APARECERÁ EN CHECADORA
                    formatoHora.format(fechaActual)
            );
        });
        timer.start();//Iniciar el timer
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {//esto no sé pq es pero chat me dijo que era importante
            new ChecadorProyecto().setVisible(true);
        });
    }
}