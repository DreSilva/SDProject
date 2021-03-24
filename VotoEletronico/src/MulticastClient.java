import java.net.*;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * The MulticastClient class joins a multicast group and loops receiving
 * messages from that group. The client also runs a MulticastUser thread that
 * loops reading a string from the keyboard and multicasting it to the group.
 * <p>
 * The example IPv4 address chosen may require you to use a VM option to
 * prefer IPv4 (if your operating system uses IPv6 sockets by default).
 * <p>
 * Usage: java -Djava.net.preferIPv4Stack=true MulticastClient
 *
 * @author Raul Barbosa
 * @version 1.0
 */
class Globals {
    public static String clientID;
    public static String command = "no cmd";
    public static boolean locked = true;
    public static String login = "empty";
    public static int n_election;
    public static String CC;
}

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;

    public static void main(String[] args) {
        Random rand = new Random();
        Globals.clientID = Integer.toString(rand.nextInt(100000000));
        System.out.println("ClientID -> " + Globals.clientID);
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastUser user = new MulticastUser();
        user.start();
    }

    public void run() {
        while (true) {
            MulticastSocket socket = null;
            try {
                socket = new MulticastSocket(PORT);  // create socket and bind it
                socket.setSoTimeout(120000);
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                socket.joinGroup(group);
                while (true) {
                    byte[] buffer = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    String[] arrOfStr = message.split("[|;]");

                    if (arrOfStr[0].equals("server") && arrOfStr[1].equals(Globals.clientID)) {
                        Globals.command = arrOfStr[3];
                        if (Globals.command.equals("logged off")) {
                            Globals.command = "login";
                            Globals.login = "off";
                            System.out.println(arrOfStr[5]);
                        } else if (Globals.command.equals("logged on & select election")) {
                            Globals.command = "election";
                            Globals.login = "on";
                            System.out.println(arrOfStr[5]);
                        } else if (Globals.command.equals("locked")) {
                            Globals.locked = false;
                            System.out.println("Terminal Desbloqueado");
                            System.out.println(arrOfStr[5]);
                            Globals.command = "login";
                        } else if (Globals.command.equals("select election")) {
                            Globals.command = "election";
                            System.out.println(arrOfStr[5]);
                        } else if (Globals.command.equals("select candidate")) {
                            System.out.println(arrOfStr[5]);
                            Globals.command = "candidate";
                        } else if (!arrOfStr[5].equals("empty")) {
                            System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                            System.out.println(arrOfStr[5]);
                        }
                    } else if (arrOfStr[1].equals("all")) {
                        Globals.command = arrOfStr[3];
                        if (Globals.command.equals("locked")) {
                            if (Globals.locked == true) {
                                //System.out.println("Terminal vai ser desbloqueado");
                                Globals.CC = arrOfStr[5];
                            }
                        }
                    }
                }
            } catch (SocketTimeoutException e) {
                if (Globals.locked == false) System.out.println("O terminal está bloqueado.");
                Globals.locked = true;
                Globals.login = "empty";
                Globals.command = "no cmd";
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;

    public MulticastUser() {
        super("User " + (long) (Math.random() * 1000));
    }

    public void run() {

        MulticastSocket socket = null;
        System.out.println(this.getName() + " ready...");
        try {
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            Scanner keyboardScanner = new Scanner(System.in);
            while (true) {
                sleep(1000);
                System.out.println(Globals.command);
                if (Globals.command.equals("locked")) {
                    if (Globals.locked) {
                        String message = "client|" + Globals.clientID + ";cmd|" + Globals.command + ";msg|" + Globals.locked;
                        byte[] buffer = message.getBytes();
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    }
                    //Globals.command = "login";
                } else if (!Globals.command.equals("no cmd")) {
                    String readKeyboard = keyboardScanner.nextLine();
                    if (Globals.command.equals("election")) Globals.n_election = Integer.parseInt(readKeyboard);
                    if (!Globals.locked) {
                        if (Globals.command.equals("candidate")) {
                            readKeyboard = "client|" + Globals.clientID + ";cmd|" + Globals.command + ";msg|" + Globals.CC + " " + Globals.n_election + " " + readKeyboard;

                        } else {
                            readKeyboard = "client|" + Globals.clientID + ";cmd|" + Globals.command + ";msg|" + readKeyboard;
                        }
                        byte[] buffer = readKeyboard.getBytes();
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    } else {
                        System.out.println("O terminal encontra-se bloqueado. Dirija-se à mesa de voto.");
                    }
                }
                if (!Globals.command.equals("login")) Globals.command = "no cmd";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
