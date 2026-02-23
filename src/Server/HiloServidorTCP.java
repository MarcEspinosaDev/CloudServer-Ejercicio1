package Server;

import java.io.*;
import java.net.*;
import java.util.Random;

public class HiloServidorTCP extends Thread {
    DataInputStream fentrada;
    DataOutputStream fsalida;
    Socket socket = null;

    private int numeroSecreto;
    private Random random;
    private String direccionCliente;

    public HiloServidorTCP(Socket s) {
        socket = s;
        random = new Random();
        numeroSecreto = random.nextInt(100) + 1;
        direccionCliente = s.getInetAddress().getHostAddress();
        try {
            fentrada = new DataInputStream(socket.getInputStream());
            fsalida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("ERROR DE E/S");
            e.printStackTrace();
        }
    }

    public void run() {
        ServidorTCP.mensaje.setText("NUMERO DE CONEXIONES ACTUALES: "
                + ServidorTCP.ACTUALES);
        try{
            fsalida.writeUTF("¡Bienvenido al juego Adivina el Número!");
            fsalida.writeUTF("Adivina un número entre 1 y 100");

            // Log en el servidor
            ServidorTCP.textarea.append("[" + direccionCliente +
                    "] Nueva partida iniciada. Número secreto: " + numeroSecreto + "\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        while (true) {
            String cadena = "";
            try {
                cadena = fentrada.readUTF();

                if (cadena.trim().equals("*")) {
                    ServidorTCP.ACTUALES--;
                    ServidorTCP.mensaje
                            .setText("NUMERO DE CONEXIONES ACTUALES: "
                                    + ServidorTCP.ACTUALES);
                    break;
                }
                if (!procesarIntento(cadena)) {
                    ServidorTCP.ACTUALES--;
                    ServidorTCP.mensaje.setText("NUMERO DE CONEXIONES ACTUALES: " + ServidorTCP.ACTUALES);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private boolean procesarIntento(String cadena){
        try{
            int intento = Integer.parseInt(cadena.trim());

            ServidorTCP.textarea.append("[" + direccionCliente + "] Intento: " + intento + "\n");

            if (intento < numeroSecreto) {
                fsalida.writeUTF("El número es mayor");
            } else if (intento > numeroSecreto) {
                fsalida.writeUTF("El número es menor");
            } else {
                fsalida.writeUTF("¡Número correcto! Era el " + numeroSecreto);
                fsalida.writeUTF("¿Quieres jugar otra vez? (si/no)");
                String respuesta = fentrada.readUTF().trim().toLowerCase();
                if (respuesta.equals("si")) {
                    numeroSecreto = random.nextInt(100) + 1;
                    ServidorTCP.textarea.append("Nuevo número secreto: " + numeroSecreto + "\n");
                    fsalida.writeUTF("Adivina un número entre 1 y 100");
                } else {
                    fsalida.writeUTF("¡Gracias por jugar!");
                    fsalida.writeUTF("*");
                    return false;
                }
            }
        } catch (NumberFormatException e){
            try{
                fsalida.writeUTF("Por favor, introduce un número válido entre 1 y 100");
                ServidorTCP.textarea.append(" → Entrada no válida\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}