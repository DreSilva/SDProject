import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import java.util.Scanner;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;
    private static DepMesa depMesa = new DepMesa();

    public static void main(String[] args) {
        if (args.length == 1) {
            depMesa.setDepartamento(args[0]);
            Date date = new Date();
            depMesa.setId(date.getTime());
        } else {
            System.out.println("Corra com o número de departamento: java MulticastServer <departamento>");
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
            voto.addMesa(depMesa);


            byte[] buffer;
            InetAddress group;
            DatagramPacket packet;

            while (true) {
                try {
                    //recebe mensagem
                    byte[] bufferR = new byte[256];
                    DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                    socketR.receive(packetR);
                    messageR = new String(packetR.getData(), 0, packetR.getLength());
                    arrOfStr = messageR.split("[|;]");
                    if (!arrOfStr[0].equals("server")) {
                        clientID = arrOfStr[1];
                        cmd = arrOfStr[3];
                        if (cmd.equals("election")) {
                            int n = Integer.parseInt(arrOfStr[5]);
                            message = "server|" + clientID + ";cmd|select candidate;msg|Selecione o candidato:\n" + voto.listaCandidatos(n - 1);
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        } else if (cmd.equals("candidate")) {
                            arrOfStr = arrOfStr[5].split(" ");
                            message = "server|" + clientID + ";cmd|select election;msg|" + voto.votar(Integer.parseInt(arrOfStr[2]) - 1, arrOfStr[0], Integer.parseInt(arrOfStr[1]) - 1, depMesa) + "\nSelecione a eleição em que pretende votar:\n" + voto.listarEleicoes();
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        } else if (cmd.equals("fail")) {
                            arrOfStr = arrOfStr[5].split(" ");
                            if (arrOfStr[1] == "null") {
                                Fail fail = new Fail(arrOfStr[0], arrOfStr[1], clientID);
                                //fail.start();
                            }
                        }
                    }

                } catch (ConnectException e) {
                    voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
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

    public Console() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        try {
            //multicast part
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
            InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
            socketR.joinGroup(groupR);

            //part to connect to the rmi server
            Voto voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");

            while (true) {
                try {

                    //pedir nr do CC do eleitor
                    System.out.println("Insira o número do CC do eleitor: ");
                    Scanner scan = new Scanner(System.in);
                    String CC = scan.nextLine();

                    if (voto.identificarLeitor(CC)) {
                        Login login = new Login(CC);
                        login.start();
                    } else {
                        System.out.println("Eleitor não identificado.");
                    }
                    sleep(1000);
                } catch (ConnectException e) {
                    voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
                }
            }

        } catch (IOException | NotBoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}

class Login extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private String CC;

    public Login(String CC) {
        super("Server " + (long) (Math.random() * 1000));
        this.CC = CC;
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
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

            //seleção do terminal de voto
            message = "server|all;cmd|locked;msg|" + this.CC;
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

            message = "server|" + clientID + ";cmd|locked;msg|Insira login no formato <username>/<password>:";
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
                    if (voto.login(arrOfStr[0], arrOfStr[1], this.CC)) {
                        //resposta
                        message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Selecione a eleição em que pretende votar:\n" + voto.listarEleicoes();
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
                } catch (IndexOutOfBoundsException e) {
                    message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                    buffer = message.getBytes();
                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                } catch (ConnectException e) {
                    voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
                }
            } while (message.equals("server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: "));
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}

class Fail extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;
    private String CC;
    private String state;
    private String clientID;

    public Fail(String CC, String state, String clientID) {
        super("Server " + (long) (Math.random() * 1000));
        this.CC = CC;
        this.state = state;
        this.clientID = clientID;
    }

    public void run() {
        MulticastSocket socket = null, socketR = null;
        try {
            boolean flag = false;
            String str ="a a";
            String[] arrOfStr=str.split(" ");
            while (!flag) {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
                InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
                socketR.joinGroup(groupR);
                socketR.setSoTimeout(10000);

                String message = "server|" + this.clientID + ";cmd|fail;msg|" + this.CC + " " + this.state;
                byte[] buffer = message.getBytes();
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
                try {
                    flag = true;
                    do {
                        byte[] bufferR = new byte[256];
                        DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                        socketR.receive(packetR);
                        String messageR = new String(packetR.getData(), 0, packetR.getLength());
                        arrOfStr = messageR.split("[|;]");
                    } while (!arrOfStr[0].equals("client") || !arrOfStr[1].equals(clientID) || !arrOfStr[3].equals("recovered"));
                    System.out.println("O terminal " + arrOfStr[1] + "está desbloqueado para o CC " + this.CC);
                } catch (SocketTimeoutException e) {
                    flag = false;
                }
            }
            System.out.println("\nO cliente " + this.clientID + " foi recuperado.\n");


            if (state.equals("locked")) {
                Voto voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
                try {
                    arrOfStr = arrOfStr[5].split("/");
                    if (voto.login(arrOfStr[0], arrOfStr[1], this.CC)) {
                        //resposta
                        String message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Selecione a eleição em que pretende votar:\n" + voto.listarEleicoes();
                        byte[] buffer = message.getBytes();
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    } else {
                        String message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                        byte[] buffer = message.getBytes();
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    }
                } catch (IndexOutOfBoundsException e) {
                    String message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                    byte[] buffer = message.getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                } catch (ConnectException e) {
                    voto = (Voto) LocateRegistry.getRegistry(7000).lookup("votacao");
                }
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //do nothing
        } finally {
            socket.close();
            socketR.close();
        }
    }
}