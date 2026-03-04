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

<<<<<<< HEAD
=======
    //private int aciertos;

>>>>>>> da9b478 (syncronized)
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
        System.out.println("NUMERO DE CONEXIONES ACTUALES: "
                + ServidorTCP.ACTUALES);
        try{
            fsalida.writeUTF("¡Bienvenido al juego Adivina el Número!");
            fsalida.writeUTF("Adivina un número entre 1 y 100");

            // Log en el servidor
            System.out.println("[" + direccionCliente +
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
                    System.out.println("NUMERO DE CONEXIONES ACTUALES: "
                                    + ServidorTCP.ACTUALES);
                    break;
                }
                if (!procesarIntento(cadena)) {
                    ServidorTCP.ACTUALES--;
                    System.out.println("NUMERO DE CONEXIONES ACTUALES: " + ServidorTCP.ACTUALES);
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

            System.out.println("[" + direccionCliente + "] Intento: " + intento + "\n");

            if (intento < numeroSecreto) {
                fsalida.writeUTF("El número es mayor");
            } else if (intento > numeroSecreto) {
                fsalida.writeUTF("El número es menor");
            } else {
                fsalida.writeUTF("¡Número correcto! Era el " + numeroSecreto);
<<<<<<< HEAD
=======
                aciertos();
>>>>>>> da9b478 (syncronized)
                fsalida.writeUTF("¿Quieres jugar otra vez? (si/no)");
                String respuesta = fentrada.readUTF().trim().toLowerCase();
                if (respuesta.equals("si")) {
                    numeroSecreto = random.nextInt(100) + 1;
                    System.out.println("Nuevo número secreto: " + numeroSecreto + "\n");
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
                System.out.println(" → Entrada no válida\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
<<<<<<< HEAD
=======

    // Genarar un numero contador para saber cuantos aciertos hay
    public synchronized void aciertos() throws IOException {
        ServidorTCP.ACTUALES++;
        fsalida.writeUTF("Cantidad de aciertos: "+ ServidorTCP.ACTUALES + "\n");
    }
>>>>>>> da9b478 (syncronized)
}