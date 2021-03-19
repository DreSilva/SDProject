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
    private static DepMesa depMesa = new DepMesa();

    public static void main(String[] args) {
        if(args.length==1){
            depMesa.setDepartamento(args[0]);
        }
        else{
            System.out.println("Corra com com o número de departamento: java MulticastServer <departamento>");
            System.exit(0);
        }
        MulticastServer server = new MulticastServer();
        server.start();
        Console console = new Console();
        console.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        long counter = 0;
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
                    if (cmd == "election") {
                        int n = Integer.parseInt(arrOfStr[5]);
                        message = "server|" + clientID + ";cmd|select candidate;msg|" + voto.listaCandidatos(n);
                        buffer = message.getBytes();
                        group = InetAddress.getByName(MULTICAST_ADDRESS);
                        packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    }else if (cmd == "candidate") {
                        arrOfStr = arrOfStr[5].split();
                        int n = Integer.parseInt(arrOfStr[5]);
                        message = "server|" + clientID + ";cmd|select candidate;msg|" + voto.votar(Integer.parseInt(arrOfStr[2]),arrOfStr[0],Integer.parseInt(arrOfStr[1]),depMesa);
                        buffer = message.getBytes();
                        group = InetAddress.getByName(MULTICAST_ADDRESS);
                        packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);

                        int n = Integer.parseInt(arrOfStr[5]);
                        message = "server|" + clientID + ";cmd|select candidate;msg|" + voto.listaCandidatos(n);
                        buffer = message.getBytes();
                        group = InetAddress.getByName(MULTICAST_ADDRESS);
                        packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
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

    public Console() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        long counter = 0;
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
                    } while (arrOfStr[0].equals("server") || (arrOfStr[3].equals("locked") && arrOfStr[5].equals("false")));

                    clientID = arrOfStr[1];

                    message = "server|" + clientID + ";cmd|locked;msg|Insira login no formato <username>/<password>/<CC>:";
                    buffer = message.getBytes();
                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                    System.out.println("O terminal " + clientID + " está desbloqueado.");

                    //login starts
                    do {
                        do {
                            byte[] bufferR = new byte[256];
                            DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                            socketR.receive(packetR);
                            messageR = new String(packetR.getData(), 0, packetR.getLength());
                            arrOfStr = messageR.split("[|;]");
                        } while (!arrOfStr[0].equals("client") || !arrOfStr[1].equals(clientID) || !arrOfStr[3].equals("login"));

                        try {
                            arrOfStr = arrOfStr[5].split("/");
                            if (CC.equals(arrOfStr[2])) {
                                if (voto.login(arrOfStr[0], arrOfStr[1], arrOfStr[2])) {
                                    //resposta
                                    message = "server|" + clientID + ";cmd|logged on;msg|Welcome to eVoting";
                                    buffer = message.getBytes();
                                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                    socket.send(packet);
                                    message = "server|" + clientID + ";cmd|select election;msg|" + voto.listarEleicoes();
                                    buffer = message.getBytes();
                                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                    socket.send(packet);
                                } else {
                                    message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                                    buffer = message.getBytes();
                                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                    socket.send(packet);
                                }
                            } else {
                                message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                                buffer = message.getBytes();
                                group = InetAddress.getByName(MULTICAST_ADDRESS);
                                packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                socket.send(packet);
                            }
                        }catch (IndexOutOfBoundsException e){
                            message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    }while(message.equals("server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: "));
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