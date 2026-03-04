package Server;

import java.io.*;
import java.net.*;

public class ServidorTCP{
    static ServerSocket servidor;
    static final int PUERTO = 44444;
    static int actuales = 0;
    static int aciertos = 0;

    public static void main(String args[]) throws IOException {
        servidor = new ServerSocket(PUERTO);
        System.out.println("========= Servidor TCP > Adivina el numero ========");
        System.out.println("Escuchando el puerto " + PUERTO);

        while(true) {
            try {
                Socket s = servidor.accept();
                actuales ++;
                System.out.println("Cliente conectado " +s.getInetAddress().getHostAddress()+
                        ", Conexiones activas: " +actuales);
                HiloServidorTCP hilo = new HiloServidorTCP(s);
                hilo.start();
            } catch (SocketException se) {
                break;
            }
        }
        System.out.println("Servidor finalizado...");
    }
}