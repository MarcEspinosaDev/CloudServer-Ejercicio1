package Server;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class ServidorTCP extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    static ServerSocket servidor;
    static final int PUERTO = 44444;
    static int ACTUALES = 0;

    static JTextField mensaje=new JTextField("");
    private JScrollPane scrollpane1;
    static JTextArea textarea;
    JButton salir = new JButton("Salir");

    // Constructor
    public ServidorTCP() {
        super(" Server - Adivina el numero ");
        setLayout(null);

        mensaje.setBounds(10, 10, 400, 30);
        add(mensaje);mensaje.setEditable(false);
        mensaje.setEditable(false);

        textarea = new JTextArea();
        scrollpane1 = new JScrollPane(textarea);
        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);

        salir.setBounds(420, 10, 100, 30);
        add(salir);

        textarea.setEditable(false);
        salir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    // accion cuando pulsamos boton
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salir) {
            try {
                servidor.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        }
    }

    public static void main(String args[]) throws IOException {
        servidor = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado");

        ServidorTCP pantalla = new ServidorTCP();
        pantalla.setBounds(0 ,0, 540, 400);
        pantalla.setVisible(true);

        mensaje.setText("NÚMERO DE CONEXIONES ACTUALES: 0");
        textarea.append("=== SERVIDOR ADIVINA EL NÚMERO ===\n");
        textarea.append("Esperando clientes...\n\n");

        while(true) {
            try {
                Socket s = servidor.accept();
                textarea.append("Cliente conectado");
                ACTUALES ++;
                HiloServidorTCP hilo = new HiloServidorTCP(s);
                hilo.start();
            } catch (SocketException se) {
                break;
            }
        }
        System.out.println("Servidor finalizado...");
    }
}