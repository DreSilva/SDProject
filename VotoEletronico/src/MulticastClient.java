import java.net.*;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.SQLOutput;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
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
    public static String CC = null;
}

public class MulticastClient extends Thread {
    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;

    public static void main(String[] args) {
        if (args.length == 1) {
            Globals.clientID = args[0];
        } else {
            System.out.println("Corra com o número de departamento: java MulticastClient <clientID>");
            System.exit(0);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    String message;
                    MulticastSocket socket = new MulticastSocket();  // create socket without binding it (only for sending)
                    if (Globals.login.equals("on")) {
                        message = "client|" + Globals.clientID + ";cmd|fail;msg|" + Globals.CC + " login";
                    } else if (!Globals.locked) {
                        message = "client|" + Globals.clientID + ";cmd|fail;msg|" + Globals.CC + " locked";
                    } else {
                        message = "client|" + Globals.clientID + ";cmd|fail;msg|null null";
                    }
                    System.out.println(message);
                    byte[] buffer = message.getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Random rand = new Random();
        System.out.println("ClientID -> " + Globals.clientID);
        MulticastClient client = new MulticastClient();
        client.start();
        MulticastUser user = new MulticastUser();
        user.start();
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                String[] arrOfStr = message.split("[|;]");


                if (arrOfStr[0].equals("server") && arrOfStr[1].equals(Globals.clientID)) {
                    //Globals.command = arrOfStr[3];
                    if (arrOfStr[3].equals("logged off")) {
                        Globals.command = "login";
                        Globals.login = "off";
                        System.out.println(arrOfStr[5]);
                    } else if (arrOfStr[3].equals("logged on & select election")) {
                        Globals.command = "election";
                        Globals.login = "on";
                        System.out.println(arrOfStr[5]);
                    } else if (arrOfStr[3].equals("locked")) {
                        Globals.locked = false;
                        System.out.println("Terminal Desbloqueado");
                        System.out.println(arrOfStr[5]);
                        Globals.command = "login";
                    } else if (arrOfStr[3].equals("select election")) {
                        Globals.command = "election";
                        System.out.println(arrOfStr[5]);
                    } else if (arrOfStr[3].equals("select candidate")) {
                        System.out.println(arrOfStr[5]);
                        Globals.command = "candidate";
                    } else if (arrOfStr[3].equals("fail")) {
                        arrOfStr = arrOfStr[5].split(" ");
                        if (arrOfStr[1].equals("login")) {
                            Globals.CC = arrOfStr[1];
                            Globals.login = "on";
                            Globals.locked = false;
                            System.out.println("O utilizador com CC" + Globals.CC + "está logged on");
                        } else {
                            Globals.CC = arrOfStr[1];
                            Globals.login = "off";
                            Globals.locked = false;
                            System.out.println("Terminal desbloqueado.");
                            System.out.println("Insira login no formato <username>/<password>:");
                        }
                        Globals.command = "recovered";
                    }
                } else if (arrOfStr[1].equals("all") && Globals.locked) {
                    Globals.command = arrOfStr[3];
                    if (arrOfStr[3].equals("locked")) {
                        //System.out.println("Terminal vai ser desbloqueado");
                        Globals.CC = arrOfStr[5];
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS = "224.0.224.0";
    private int PORT = 4321;

    public MulticastUser() {
        super("User " + (long) (Math.random() * 1000));
    }

    public String getTimeConsole(Scanner scanner, int time) throws NoSuchElementException, ExecutionException, InterruptedException {
        String result;
        try {

            FutureTask<String> task = new FutureTask<>(() -> {
                return scanner.nextLine();
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
            result = task.get(time, TimeUnit.SECONDS);

            return result;
        } catch (TimeoutException e) {
            System.out.println("O terminal bloqueou por inatividade. Dirija-se à mesa de voto.");
            Globals.locked = true;
            Globals.command = "no cmd";
            Globals.login = "empty";
            Globals.CC = null;
        }
        return null;
    }

    public void run() {
        while (true) {
            MulticastSocket socket = null;
            try {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                Scanner keyboardScanner = new Scanner(System.in);
                while (true) {
                    sleep(1000);
                    if (Globals.command.equals("locked")) {
                        if (Globals.locked) {
                            String message = "client|" + Globals.clientID + ";cmd|" + Globals.command + ";msg|" + Globals.locked;
                            byte[] buffer = message.getBytes();
                            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    } else if (Globals.command.equals("recovered")) {
                        if (Globals.login.equals("on")) {
                            String message = "client|" + Globals.clientID + ";cmd|recovered;msg|empty";
                            byte[] buffer = message.getBytes();
                            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }

                    } else if (!Globals.command.equals("no cmd")) {
                        String readKeyboard = keyboardScanner.nextLine();//getTimeConsole(keyboardScanner, 10);
                        if (readKeyboard != null){
                            if (Globals.command.equals("election")) Globals.n_election = Integer.parseInt(readKeyboard);
                            if (!Globals.locked) {
                                if (Globals.command.equals("recovered")) {
                                    if (Globals.login.equals("off")) {
                                        readKeyboard = "client|" + Globals.clientID + ";cmd|" + Globals.command + ";msg|" + readKeyboard;
                                    }
                                } else if (Globals.command.equals("candidate")) {
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
                    }
                    if (!Globals.command.equals("login") && Globals.command.equals("recovered")) Globals.command = "no cmd";
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
}
