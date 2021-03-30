import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;


public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS,serverAddress;
    private int PORT,RMIPORT;
    private static final DepMesa depMesa = new DepMesa();


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

            //e.printStackTrace();
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
        portoInString = prop.getProperty("portoRMI");
        this.RMIPORT = Integer.parseInt(portoInString);
        this.MULTICAST_ADDRESS = prop.getProperty(depMesa.departamento);
        this.serverAddress = prop.getProperty("RMIAddress");
        if(this.MULTICAST_ADDRESS==null){
            System.out.println("Esse departamente nao se encontra no sistema");
        }

    }

    /**
     * Cria o listener para o ctrl c para notifição de fecho da mesa
     * Lê o file de configs para obter a informação associada ao departamento
     * Lança a thread Multicast server(recebe e responde a requests enviadas pelos clientes)
     * Lança a thread Console(lê CC's da consola do server para fazer a identificação e login do eleitor)
     */
    public static void main(String[] args) throws IOException {
        //argumentos
        if (args.length == 1) {
            depMesa.setDepartamento(args[0]);
            Date date = new Date();
            depMesa.setId(date.getTime());
        } else {
            System.out.println("Corra com o número de departamento: java MulticastServer <departamento>");
            System.exit(0);
        }

        //inicialização das threads
        MulticastServer server = new MulticastServer();
        server.readConfigs();
        server.start();
        Console console = new Console(server.MULTICAST_ADDRESS, server.PORT,server.RMIPORT, server.serverAddress);
        console.start();

        //listener para ctrl C
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Voto voto = (Voto) LocateRegistry.getRegistry(server.serverAddress,server.RMIPORT).lookup("votacao");
                    voto.removeMesa(depMesa);
                } catch (RemoteException e) {
                    //e.printStackTrace();
                } catch (NotBoundException e) {
                    //e.printStackTrace();
                }
            }
        });
    }

    /**
     * construtor da class Multicast server
     */
    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    /**
     * thread responsável pelas resposta aos pedidos enviados pelos clientes
     */
    public void run() {
        MulticastSocket socket = null, socketR = null;
        String message, messageR;
        String[] arrOfStr;
        String clientID = "", cmd;
        try {
            //multicast part
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
            InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
            socketR.joinGroup(groupR);

            //part to connect to the rmi server
            Voto voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");
            message=voto.addMesa(depMesa);
            if(!message.equals("")){
                System.out.println("Ja se encontra uma mesa de voto neste departamento");
                System.exit(0);
            }


            byte[] buffer;
            InetAddress group;
            DatagramPacket packet;
            boolean voting = false;
            String response = "";
            int n=0;

            //ciclo de requests e answers
            while (true) {
                try {
                    voting = false;
                    //recebe mensagem
                    byte[] bufferR = new byte[256];
                    DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                    socketR.receive(packetR);
                    messageR = new String(packetR.getData(), 0, packetR.getLength());
                    arrOfStr = messageR.split("[|;]");

                    //resposta à mensagem
                    if (!arrOfStr[0].equals("server")) {
                        clientID = arrOfStr[1];
                        cmd = arrOfStr[3];
                        if (cmd.equals("election")) {
                            //resposta a pedido dos candidatos de determinada eleição
                            n = Integer.parseInt(arrOfStr[5]);
                            if(!voto.listaCandidatos(n-1).equals("")) {
                                message = "server|" + clientID + ";cmd|select candidate;msg|Selecione o candidato:\n" + voto.listaCandidatos(n - 1);
                                buffer = message.getBytes();
                                group = InetAddress.getByName(MULTICAST_ADDRESS);
                                packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                socket.send(packet);
                            }
                            else{
                                message = "server|" + clientID + ";cmd|select candidate;msg|Nao ha eleicoes para votar\n";
                                buffer = message.getBytes();
                                group = InetAddress.getByName(MULTICAST_ADDRESS);
                                packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                socket.send(packet);
                            }
                        } else if (cmd.equals("candidate")) {
                            //resposta ao voto realizado
                            voting = true;
                            arrOfStr = arrOfStr[5].split(" ");
                            response  = voto.votar(Integer.parseInt(arrOfStr[2]) - 1, arrOfStr[0], Integer.parseInt(arrOfStr[1]) - 1, depMesa);
                            message = "server|" + clientID + ";cmd|select election;msg|" + response + "\nSelecione a eleicao em que pretende votar:\n" + voto.listarEleicoes();
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                            response="";
                        } else if (cmd.equals("fail")) {
                            //em caso de falha do cliente cria-se a thread fail para recuperação do mesmo
                            arrOfStr = arrOfStr[5].split(" ");
                            if (!arrOfStr[1].equals("null")) {
                                Fail fail = new Fail(arrOfStr[0], arrOfStr[1], clientID,MULTICAST_ADDRESS,PORT,RMIPORT,serverAddress);
                                fail.start();
                            }
                        }
                    }

                } catch (ConnectException e) {
                    //recuperação em caso de falha do rmi
                    Date now = new Date();
                    Date after = new Date();
                    after.setTime(now.getTime()+30000);
                    boolean flag = false;
                    while (now.before(after)) {
                        now = new Date();
                        try {
                            voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");
                            flag = true;
                            break;
                        }catch (ConnectException e1){
                            ;
                        }
                    }
                    if(!flag){
                        System.out.println("Erro nos servidores RMI!");
                        System.exit(0);
                    }
                    else {
                        if (voting && response.equals("") && voto != null) {
                            //perda do voto por falha dos dois rmis
                            message = "server|" + clientID + ";cmd|votelost;msg|Selecione o candidato:\n" + voto.listaCandidatos(n - 1);
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    }

                }
            }
        } catch (IOException | NotBoundException e) {
            //e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}

class Console extends Thread {
    private String MULTICAST_ADDRESS,serverAddress;
    private int PORT,RMIPORT;


    /**
     * construtor da classe console
     */
    public Console(String MULTICAST_ADDRESS,int PORT,int RMIPORT,String serverAddress) {
        super("Server " + (long) (Math.random() * 1000));
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
        this.RMIPORT = RMIPORT;
        this.serverAddress = serverAddress;
    }

    /**
     * Thread reponsável pela leitura de CC's da consola e chamada de threads para executar logins
     */
    public void run() {
        MulticastSocket socket = null, socketR = null;
        try {
            //multicast part
            socket = new MulticastSocket();  // create socket without binding it (only for sending)
            socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
            InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
            socketR.joinGroup(groupR);

            //part to connect to the rmi server
            Voto voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");

            while (true) {
                try {
                    //pedir nr do CC do eleitor
                    System.out.println("Insira o número do CC do eleitor: ");
                    Scanner scan = new Scanner(System.in);
                    String CC = scan.nextLine();

                    //identificação do leitor e em caso de sucesso cria-se a thread de login
                    if (voto.identificarLeitor(CC)) {
                        Login login = new Login(CC,MULTICAST_ADDRESS,PORT,RMIPORT,serverAddress);
                        login.start();
                    } else {
                        System.out.println("Eleitor nao identificado.");
                    }
                    sleep(1000);
                } catch (ConnectException e) {
                    //em caso de falha do servidor RMI primário
                    Date now = new Date();
                    Date after = new Date();
                    after.setTime(now.getTime()+30000);
                    boolean flag = false;
                    while (now.before(after)) {
                        now = new Date();
                        try {
                            voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");
                            flag = true;
                            break;
                        }catch (ConnectException e1){
                            ;
                        }
                    }
                    if(!flag){
                        System.out.println("Erro nos servidores RMI!");
                        System.exit(0);
                    }
                }
                catch (NoSuchElementException e){

                }
            }

        } catch (IOException | NotBoundException | InterruptedException e) {
            //e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}

class Login extends Thread {
    private String MULTICAST_ADDRESS,serverAddress;
    private int PORT,RMIPORT;
    private final String CC;


    /**
     * Construtor
     * @param CC Cartao de Cidadao do user
     * @param MULTICAST_ADDRESS Adress Multicast do departamento
     * @param PORT Porto Multicast do Departamento
     * @param RMIPORT Porto do Servidor RMI
     */
    public Login(String CC,String MULTICAST_ADDRESS,int PORT,int RMIPORT,String serverAddress) {
        super("Server " + (long) (Math.random() * 1000));
        this.MULTICAST_ADDRESS = MULTICAST_ADDRESS;
        this.PORT = PORT;
        this.RMIPORT = RMIPORT;
        this.CC = CC;
        this.serverAddress = serverAddress;
    }

    /**
     * Threads responsáveis pela execução do login dos clientes
     */
    public void run() {
        MulticastSocket socket = null, socketR = null;
        String message, messageR;
        String[] arrOfStr;
        String clientID = null;
        try {
            boolean flag_r = false;
            String str ="a a";
            arrOfStr=str.split(" ");

            //repete envio até haver sucesso
            while (!flag_r) {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
                InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
                socketR.joinGroup(groupR);
                socketR.setSoTimeout(2500);

                Voto voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");

                //mensagem geral para todos os clientes para saber quais se encontram disponiveis
                message = "server|all;cmd|locked;msg|" + this.CC;
                byte[] buffer = message.getBytes();
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);

                //espera resposta
                try {
                    //recebe resposta
                    flag_r = true;
                    //espera resposta de um cliente livre
                    do {
                        byte[] bufferR = new byte[256];
                        DatagramPacket packetR = new DatagramPacket(bufferR, bufferR.length);
                        socketR.receive(packetR);
                        messageR = new String(packetR.getData(), 0, packetR.getLength());
                        arrOfStr = messageR.split("[|; ]");
                    } while (arrOfStr[0].equals("server") || (arrOfStr[3].equals("locked") && arrOfStr[5].equals("false")));
                    clientID = arrOfStr[1];
                } catch (SocketTimeoutException e) {
                    // deu time out e reenvia a mensagem
                    flag_r = false;
                }
            }

            socketR.setSoTimeout(0);

            Voto voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");

            //envia mensagem de inicio de login ao cliente
            message = "server|" + clientID + ";cmd|locked;msg|Insira login no formato <username>/<password>:";
            byte[] buffer = message.getBytes();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);
            System.out.println("O terminal " + clientID + " esta desbloqueado.");

            //espera resposta do cliente em questão com a password correta
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
                        //em caso de login correto
                        if(! voto.listarEleicoes().equals("")) {
                            message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Selecione a eleicao em que pretende votar:\n" + voto.listarEleicoes();
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                        else{
                            message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Nao ha eleicoes disponiveis\n";
                            buffer = message.getBytes();
                            group = InetAddress.getByName(MULTICAST_ADDRESS);
                            packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                            socket.send(packet);
                        }
                    } else {
                        //em caso de erro no login
                        message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                        buffer = message.getBytes();
                        group = InetAddress.getByName(MULTICAST_ADDRESS);
                        packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    }
                } catch (IndexOutOfBoundsException e) {
                    //em caso de erro no formato
                    message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                    buffer = message.getBytes();
                    group = InetAddress.getByName(MULTICAST_ADDRESS);
                    packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                } catch (ConnectException e) {
                    //em caso de falha do RMI
                    Date now = new Date();
                    Date after = new Date();
                    after.setTime(now.getTime()+30000);
                    boolean flag = false;
                    while (now.before(after)) {
                        now = new Date();
                        try {
                            voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");
                            flag = true;
                            break;
                        }catch (ConnectException e1){
                            ;
                        }
                    }
                    if(!flag){
                        System.out.println("Erro nos servidores RMI!");
                        System.exit(0);
                    }
                }
            } while (message.equals("server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: "));
        } catch (IOException | NotBoundException e) {
           // e.printStackTrace();
        } finally {
            socket.close();
            socketR.close();
        }
    }
}

class Fail extends Thread {
    private final String CC;
    private String state;
    private final String clientID;

    private String MULTICAST_ADDRESS, serverAddress;
    private int PORT,RMIPORT;


    /**
     * Construtor da class fail
     * @param CC CC CC preciso para a recuperação do cliente
     * @param state estado do cliente antes de ser fechado
     * @param clientID ID do cliente que está a ser rescuperado
     * @param MULTICAST_ADDRESS endereço da rede multicast
     * @param PORT port da rede multicast
     * @param RMIPORT porto rmi
     */
    public Fail(String CC, String state, String clientID,String MULTICAST_ADDRESS,int PORT, int RMIPORT,String serverAddress) {
        super("Server " + (long) (Math.random() * 1000));
        this.CC = CC;
        this.state = state;
        this.clientID = clientID;
        this.RMIPORT=RMIPORT;
        this.MULTICAST_ADDRESS=MULTICAST_ADDRESS;
        this.PORT=PORT;
        this.serverAddress = serverAddress;
    }

    /**
     * Threads responsáveis pela recuperação dos clientes
     */
    public void run() {
        MulticastSocket socket = null, socketR = null;
        try {
            //Se o cliented estivesse apenas unlocked sem login é preciso concluir o login
            Voto voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");
            boolean flag = false;
            String str ="a a";
            String[] arrOfStr=str.split(" ");

            //repete envio até haver sucesso
            while (!flag) {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
                InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
                socketR.joinGroup(groupR);
                socketR.setSoTimeout(2500);

                //envio da mensagem para recuperação
                String message = "server|" + this.clientID + ";cmd|fail;msg|" + this.CC + " " + this.state;
                byte[] buffer = message.getBytes();
                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);

                //espera resposta
                try {
                    //recebe resposta
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
                    // deu time out e reenvia a mensagem
                    flag = false;
                }
            }

            //recuperação com sucesso
            System.out.println("\nO cliente " + this.clientID + " foi recuperado.\n");



            if (state.equals("locked")) {
                socket = new MulticastSocket();  // create socket without binding it (only for sending)
                socketR = new MulticastSocket(PORT); // server is gonna receive its own messages
                InetAddress groupR = InetAddress.getByName(MULTICAST_ADDRESS);
                socketR.joinGroup(groupR);
                String message,messageR;
                byte[] buffer;

                InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                DatagramPacket packet;

                message = "server|" + clientID + ";cmd|locked;msg|Insira login no formato <username>/<password>:";
                buffer = message.getBytes();
                group = InetAddress.getByName(MULTICAST_ADDRESS);
                packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);
                System.out.println("O terminal " + clientID + " esta desbloqueado.");

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
                            if(! voto.listarEleicoes().equals("")) {
                                message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Selecione a eleicao em que pretende votar:\n" + voto.listarEleicoes();
                                buffer = message.getBytes();
                                group = InetAddress.getByName(MULTICAST_ADDRESS);
                                packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                                socket.send(packet);
                            }
                            else{
                                message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Nao ha eleicoes disponiveis\n";
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
                    } catch (IndexOutOfBoundsException e) {
                        message = "server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: ";
                        buffer = message.getBytes();
                        group = InetAddress.getByName(MULTICAST_ADDRESS);
                        packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                        socket.send(packet);
                    } catch (ConnectException e) {
                        //em caso de falha do RMI
                        Date now = new Date();
                        Date after = new Date();
                        after.setTime(now.getTime()+30000);
                        boolean flag2 = false;
                        while (now.before(after)) {
                            now = new Date();
                            try {
                                voto = (Voto) LocateRegistry.getRegistry(this.serverAddress,this.RMIPORT).lookup("votacao");
                                flag2 = true;
                                break;
                            }catch (ConnectException e1){
                                ;
                            }
                        }
                        if(!flag2){
                            System.out.println("Erro nos servidores RMI!");
                            System.exit(0);
                        }
                    }
                } while (message.equals("server|" + clientID + ";cmd|logged off;msg|Wrong Login.Try again: "));
            }
            else if(state.equals("login")){
                //em caso já estar login enviar a lista de candidatos
                if(! voto.listarEleicoes().equals("")) {
                    String message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Selecione a eleicao em que pretende votar:\n" + voto.listarEleicoes();
                    byte[] buffer = message.getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                }
                else{
                    String message = "server|" + clientID + ";cmd|logged on & select election;msg|Welcome to eVoting. Nao ha eleicoes para votar\n";
                    byte[] buffer = message.getBytes();
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                    socket.send(packet);
                }
            }
        } catch (NotBoundException e) {
           // e.printStackTrace();
        } catch (IOException e) {
            //do nothing
        }
        finally {
            socket.close();
            socketR.close();
        }
    }
}