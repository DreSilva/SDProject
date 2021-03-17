import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        long counter = 0;
        System.out.println(this.getName() + " running...");
        String message, messageR;
        String[] arrOfStr;
        String clientID, cmd;
        try {
            //multicast part
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
            InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
            socketR.joinGroup(groupR);

            //part to connect to the rmi server
            Voto voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");

            byte[] buffer;
            InetAddress group;
            DatagramPacket packet;

            while (true) {
                //recebe mensagem
                byte[] bufferR = new byte[256];
                DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                socketR.receive(packetR);
                messageR = new String(packetR.getData(), 0, packetR.getLength());
                arrOfStr = messageR.split("[|; ]");
                if (arrOfStr[0] != "server") {
                    clientID = arrOfStr[1];
                    cmd = arrOfStr[3];
                    if (cmd == "login") {
                        arrOfStr = arrOfStr[5].split(" ");
                        if (voto.login(arrOfStr[0], arrOfStr[1], arrOfStr[2])) {
                            //resposta
                            message = "client|" + arrOfStr[1] + ";logged|on;msg|Welcome to eVoting";
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        } else {
                            message = "client|" + arrOfStr[1] + ";logged|off;msg|Wrong Login.Try again: \n";
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    }
                }
            }
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}

class Console extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public static void main(String[] args) {
        Console console = new Console();
        console.start();
    }

    public Console() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        long counter = 0;
        System.out.println(this.getName() + " running...");
        String message, messageR;
        String[] arrOfStr;
        String clientID;
        try {
            //multicast part
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
            InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
            socketR.joinGroup(groupR);

            //part to connect to the rmi server
            Voto voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");

            while (true) {

                //pedir nr do CC do eleitor
                System.out.print("Insira o número do CC do eleitor: ");
                Scanner scan = new Scanner(System.in);
                String CC = scan.nextLine();

                if (voto.identificarLeitor(CC)) {
                    //seleção do terminal de voto
                    message = "server|all;cmd|locked;msg|empty";
                    byte[] buffer = message.getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);

                    do {
                        byte[] bufferR = new byte[256];
                        DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                        socketR.receive(packetR);
                        messageR = new String(packetR.getData(), 0, packetR.getLength());
                        arrOfStr = messageR.split("[|; ]");
                    } while (arrOfStr[3] == "locked" && arrOfStr[5] == "true");

                    clientID = arrOfStr[1];

                    message = "server|" + clientID + ";cmd|locked;msg|Insira login no formato <username> <password> <CC>\n";
                    buffer = message.getBytes();
                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                    System.out.println("O terminal " + arrOfStr[1] + " está desbloqueado.");
                } else {
                    System.out.println("Eleitor não identificado.");
                }
            }

        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}