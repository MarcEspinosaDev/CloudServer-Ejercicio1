package Cliente;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClienteTCP extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    Socket socket = null;
    DataInputStream fentrada;
    DataOutputStream fsalida;

    static JTextField campoNumero = new JTextField();
    private JScrollPane scrollpane1;
    static JTextArea textarea1;
    JButton botonEnviar = new JButton("Enviar");
    JButton botonSalir = new JButton("Salir");
    boolean repetir = true;
    boolean esperandoRespuestaSiNo = false;

    // Constructor
    public ClienteTCP(Socket s) {
        super(" JUEGO: ADIVINA EL NÚMERO ");
        setLayout(null);

        campoNumero.setBounds(10, 10, 400, 30);
        add(campoNumero);

        textarea1 = new JTextArea();
        scrollpane1 = new JScrollPane(textarea1);
        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);

        botonEnviar.setBounds(420, 10, 100, 30);
        add(botonEnviar);

        botonSalir.setBounds(420, 50, 100, 30);
        add(botonSalir);

        textarea1.setEditable(false);
        botonEnviar.addActionListener(this);
        botonSalir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        socket = s;
        try {
            fentrada = new DataInputStream(socket.getInputStream());
            fsalida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("ERROR DE E/S");
            e.printStackTrace();
            System.exit(0);
        }
    }

    // Acción cuando pulsamos botones
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonEnviar) {
            String texto = campoNumero.getText().trim();
            if (!texto.isEmpty()) {
                try {
                    fsalida.writeUTF(texto);
                    // Si está esperando respuesta si/no, actualizar el textarea
                    if (esperandoRespuestaSiNo) {
                        textarea1.append("Tu respuesta: " + texto + "\n");
                    } else {
                        textarea1.append("Tu intento: " + texto + "\n");
                    }

                    campoNumero.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (e.getSource() == botonSalir) {
            try {
                fsalida.writeUTF("*");
                repetir = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void ejecutar() {
        String texto = "";

        while (repetir) {
            try {
                texto = fentrada.readUTF();

                if (texto.trim().equals("*")) {
                    repetir = false;
                    break;
                }
                // Mostrar mensaje del servidor
                textarea1.append("Servidor: " + texto + "\n");
                // Detectar si está esperando respuesta si/no
                if (texto.contains("¿Quieres jugar otra vez?")) {
                    esperandoRespuestaSiNo = true;
                    campoNumero.setText("");
                } else if (texto.contains("Nuevo juego iniciado") ||
                        texto.contains("Adivina un número")) {
                    esperandoRespuestaSiNo = false;
                    textarea1.append("\n");
                } else if (texto.contains("¡Gracias por jugar!")) {
                    esperandoRespuestaSiNo = false;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),
                        "<<MENSAJE DE ERROR>>",
                        JOptionPane.ERROR_MESSAGE
                );
                repetir = false;
            }
        }

        // Cerrar conexión
        try {
            socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int puerto = 44444;
        String host = "localhost";

        if (args.length > 0) {
            host = args[0];
        }

        Socket s = null;
        try {
            s = new Socket(host, puerto);
            System.out.println("Conectado al servidor en " + host + ":" + puerto);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "IMPOSIBLE CONECTAR CON EL SERVIDOR\n" + e.getMessage(),
                    "<<MENSAJE DE ERROR>>",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }

        ClienteTCP cliente = new ClienteTCP(s);
        cliente.setBounds(0, 0, 540, 400);
        cliente.setVisible(true);
        cliente.ejecutar();
    }
}