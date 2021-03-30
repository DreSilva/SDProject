
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;


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
    private static String MULTICAST_ADDRESS,Departamento;
    private static int PORT;

    /**
     * Abre o ficheiro de config para leitura
     * @param fileName ficheiro para abrir
     * @return propreties file
     */
    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            // create Properties class object
            prop = new Properties();
            // load properties file into it
            prop.load(fis);

        } catch (FileNotFoundException e) {

           // e.printStackTrace();
        } catch (IOException e) {

            //e.printStackTrace();
        } finally {
            fis.close();
        }

        return prop;
    }

    /**
     * Le as configurações do ficheiro de config
     */
    public void readConfigs() throws IOException {

        Properties prop = readPropertiesFile("config.properties");
        String portoInString = prop.getProperty("portoMulticast");
        this.PORT = Integer.parseInt(portoInString);
        this.MULTICAST_ADDRESS = prop.getProperty(Departamento);
        if(MULTICAST_ADDRESS==null){
            System.out.println("Nao existe esse departamento registado");
        }

    }

    /**
     * Cria o listener para o ctrl c para eventual recuperação do cliente
     * Lê o file de configs para obter a informação associada ao departamento
     * Lança a thread MulticastClient(recebe mensagens)
     * Lança a thread MulticastServer(envia resposta)
     * @param args <clientID> ID do cliente multicast <Departamento> departamento a que oterminal de voto pertence
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //argumentos
        if (args.length == 2) {
            Globals.clientID = args[0];
            Departamento = args[1];
        } else {
            System.out.println("Corra com o número de departamento: java MulticastClient <clientID> <Departamento>");
            System.exit(0);
        }

        //listener do ctrl C
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
                    //System.out.println(message);
                    byte[] buffer = message.getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                } catch (IOException ex) {
                    //ex.printStackTrace();
                }
            }
        });

        //inicializa e começa as threads
        System.out.println("ClientID -> " + Globals.clientID);
        MulticastClient client = new MulticastClient();
        client.readConfigs();
        client.start();
        MulticastUser user = new MulticastUser(MULTICAST_ADDRESS,PORT);
        user.start();
    }

    /**
     * Thread responsável por receber mensagens do servidor, escrevê-las no terminal e interpretar os comandos do server
     */
    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                //recebe message do client
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                String[] arrOfStr = message.split("[|;]");
                Scanner scanner = new Scanner(System.in);

                //interpreta o comando do servidor e escreve a mensagem deste na consola
                if (arrOfStr[0].equals("server") && arrOfStr[1].equals(Globals.clientID)) {
                    if (arrOfStr[3].equals("logged off")) {
                        Globals.command = "login";
                        Globals.login = "off";
                        System.out.println(arrOfStr[5]);
                    } else if (arrOfStr[3].equals("logged on & select election")) {
                        if(!arrOfStr[5].equals("Welcome to eVoting. Nao ha eleicoes disponiveis\n")){
                            Globals.command = "election";
                            Globals.login = "on";
                            System.out.println(arrOfStr[5]);
                        }
                        else{
                            System.out.println(arrOfStr[5]);
                            Globals.CC=null;
                            Globals.locked=true;
                            Globals.login="empty";
                            Globals.command="no cmd";
                        }
                    } else if (arrOfStr[3].equals("locked")) {
                        Globals.locked = false;
                        System.out.println("Terminal Desbloqueado");
                        System.out.println(arrOfStr[5]);
                        Globals.command = "login";
                    } else if (arrOfStr[3].equals("select election")) {
                        System.out.println("Quer continuar a votar?");
                        int choice = 0;
                        do{
                            System.out.println("1-Sim");
                            System.out.println("2-Nao");
                            try {
                                choice = Integer.parseInt(scanner.nextLine());
                            }
                            catch (NumberFormatException  e){
                                System.out.println("Insira uma das opcoes");
                            }

                        }while(choice!=1 && choice!=2);
                        if(choice==1) {
                            Globals.command = "election";
                            System.out.println(arrOfStr[5]);
                        }
                        else{
                            Globals.CC=null;
                            Globals.locked=true;
                            Globals.login="empty";
                            Globals.command="no cmd";
                        }
                    } else if (arrOfStr[3].equals("select candidate")) {
                        if(!arrOfStr[5].equals("Nao ha eleicoes para votar\n")){
                            Globals.CC=null;
                            Globals.locked=true;
                            Globals.login="empty";
                            Globals.command="no cmd";
                        }
                        else {
                            System.out.println(arrOfStr[5]);
                            Globals.command = "candidate";
                        }
                    } else if (arrOfStr[3].equals("fail")) {
                        Globals.locked = false;
                        arrOfStr = arrOfStr[5].split(" ");
                        if (arrOfStr[1].equals("login")) {
                            Globals.CC = arrOfStr[0];
                            Globals.login = "on";
                            System.out.println("O utilizador com CC " + Globals.CC + " esta logged on");
                        } else {
                            Globals.CC = arrOfStr[0];
                            Globals.login = "off";
                        }
                        Globals.command = "recovered";
                    } else if (arrOfStr[3].equals("votelost")) {
                        System.out.println("Houve um problema ao efetuar o voto, pff vote outra vez");
                        System.out.println(arrOfStr[5]);
                        Globals.command = "candidate";
                    }
                } else if (arrOfStr[1].equals("all") && Globals.locked) {
                    Globals.command = arrOfStr[3];
                    if (arrOfStr[3].equals("locked")) {
                        Globals.CC = arrOfStr[5];
                    }
                }

            }
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}

class MulticastUser extends Thread {
    private String MULTICAST_ADDRESS;
    private int PORT;

    /**
     * Construtor do mcast user
     * @param MULTICAST_ADDRESS IP do grupo multicast
     * @param PORT Port do grupo multicast
     */
    public MulticastUser(String MULTICAST_ADDRESS,int PORT) {
        super("User " + (long) (Math.random() * 1000));
        this.MULTICAST_ADDRESS=MULTICAST_ADDRESS;
        this.PORT=PORT;
    }


    /**
     * Lê inputs da consola com um timer
     * @param keyboardScanner Scanner do teclado
     * @param time tempo em segundos para o timer
     * @return strin lida do teclado
     */
    public String readTimedConsole(NonBlockingInputStream keyboardScanner, int time) throws NoSuchElementException, IOException {
        int i;
        char c;
        StringBuilder sb = new StringBuilder();

        do{
            i = keyboardScanner.read(time*1000);
            if(i!=-2 && i!=10) {
                c=(char)i;
                sb.append(c);
            }
        }while(i!=-2 && i!=10);

        if(i==-2) return null;
        else return sb.toString().substring(0,sb.toString().length()-1);
    }

    /**
     * Função da thread que lê strings da consola e envia mensagem ao servidor
     */
    public void run() {
        while (true) {
            MulticastSocket socket = null;
            try {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                NonBlockingInputStream keyboardScanner = new NonBlockingInputStream(System.in, true);
                while (true) {
                    sleep(1000);
                    if (Globals.command.equals("locked")) {
                        if (Globals.locked) {
                            // envia a resposta caso este cliente esteja bloqueado(disponivel)
                            String message = "client|" + Globals.clientID + ";cmd|" + Globals.command + ";msg|" + Globals.locked;
                            byte[] buffer = message.getBytes();
                            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    } else if (Globals.command.equals("recovered")) {
                        //Resposta ao pedido de recuperação do client
                        String message = "client|" + Globals.clientID + ";cmd|recovered;msg|empty";
                        byte[] buffer = message.getBytes();
                        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);

                    } else if (!Globals.command.equals("no cmd")) {
                        //leitura da consola
                        String readKeyboard = readTimedConsole(keyboardScanner,120);
                        //caso não haja time out enviar resposta ao server
                        if (readKeyboard != null) {
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
                                System.out.println("O terminal encontra-se bloqueado. Dirija-se a mesa de voto.");
                            }
                        } else {
                            //bloqueio do terminal em caso de time out
                            System.out.println("O terminal bloqueou por inatividade. Dirija-se a mesa de voto");
                            Globals.CC=null;
                            Globals.locked=true;
                            Globals.login="empty";
                            Globals.command="no cmd";
                        }
                    }
                    //reset do command para não haver repetições
                    if (!Globals.command.equals("login") && Globals.command.equals("recovered"))
                        Globals.command = "no cmd";
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            } finally {
                socket.close();
            }
        }
    }
}
