import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Comparator;


/**
 * Classe que vai ficar com as variaveis que precisam de ser guardadas entre threads
 */
class Global{
    static volatile boolean prim = false;
    static volatile RMIServer rmiServer;
    static volatile ArrayList<Notifications> admin = new ArrayList<>();
    static int portoUDP;

    static {
        try {
            rmiServer = new RMIServer();
        } catch (RemoteException e) {
           //StackTrace();
        }
    }
}

/**
 * Classe para o RMI primario que vai servir como UDP server
 */
class UDPPrim extends Thread{
    DatagramSocket aSocket;

    /**
     * Construtor para o UDPrim
     * @param aSocket socket criada para o UDP server
     */
    public UDPPrim(DatagramSocket aSocket){
        this.aSocket=aSocket;
        this.start();
    }

    /**
     * UDP server vai comunicar com o UDP client de 4 a 4 segundos para verificar que está vivo, ao mesmo tempo vai
     * escrevendo no ficheiro para ir guarando os dados
     */

    @Override
    public void run() {
        String s;
        try{
            System.out.println("Socket Datagram a escuta no porto 6789");
            while(true){
                byte[] buffer = new byte[1000];
                System.out.println("Esperando pacote");
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                s=new String(request.getData(), 0, request.getLength());
                System.out.println("Server Recebeu: " + s);
                DatagramPacket reply = new DatagramPacket(request.getData(),
                        request.getLength(), request.getAddress(), request.getPort());
                aSocket.send(reply);
                Global.rmiServer.writeFile();
                Thread.sleep(4000);

            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        } catch (InterruptedException e) {
           // e.printStackTrace();
        } finally {if(aSocket != null) aSocket.close();}
    }
}

/**
 * Classe para o RMI secundario que vai servir como UDP client
 */
class UDPSec extends Thread{
    /**
     * Inicializa o cliente
     */
    public UDPSec(){this.start();}

    /**
     * UDP client vai comunicar com o UDP server de 4 a 4 segundos para verificar que ele está vivo, caso o servidor nao
     * responda nos 4 segundos é entao levantada uma excpetion que vai fazer com que o RMI secundario passe a primario
     * fazendo assim as ações dele
     */
    @Override
    public void run() {
        try (DatagramSocket aSocket = new DatagramSocket()) {

            String texto = "";
            aSocket.setSoTimeout(5000);

            while (true) {
                texto = "ping";

                byte[] m = texto.getBytes();
                InetAddress aHost = InetAddress.getByName("localhost");
                int serverPort = Global.portoUDP;
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                aSocket.send(request);
                byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                System.out.println("Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
                Thread.sleep(2500);
            } // while
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Server primario morreu");
            Global.prim=true;
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
    }
}

/**
 * Thread que vai correr toda a lista de eleições para ver se há alguma que acabou
 */
class RealTimeUpdate extends Thread{
    /**
     * Inicializa a thread
     */
    public RealTimeUpdate(){this.start();}

    /**
     * Corre a função para verificar horas da thread
     */
    @Override
    public void run() {
        while (true) {
            try {
                Global.rmiServer.realTimeEleicao();
            } catch (RemoteException e) {
               // e.printStackTrace();
            }
        }
    }
}


/**
 * Classe para o RMI server
 */
public class RMIServer extends UnicastRemoteObject implements Voto {
    static CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Eleicao> eleicoes = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Eleicao> eleicoesVelhas = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Lista> listas = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<DepMesa> mesasVoto = new CopyOnWriteArrayList<>();
    private static int portoRMI;
    private static String serverAddress;

    /**
     * Construtor para a classe
     * @throws RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public RMIServer() throws RemoteException {
        super();
    }

    /**
     *  @inheritDoc
     */
    public void subscribeAdmin(Notifications nAdmin) throws RemoteException {
        Global.admin.add(nAdmin);
    }

    /**
     *  @inheritDoc
     */
    public String listarLista(Lista lista) throws  java.rmi.RemoteException{
        StringBuilder sb = new StringBuilder();
        for(User user: lista.lista){
            sb.append("- ").append(user.user).append(" ").append(user.CC).append("\n");
        }
        return sb.toString();
    }

    /**
     *  @inheritDoc
     */
    public void writeFile() throws java.rmi.RemoteException{
        try {
            FileOutputStream f = new FileOutputStream("myObjects.ser");
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(users);
            o.writeObject(eleicoes);
            o.writeObject(eleicoesVelhas);
            o.writeObject(listas);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    /**
     * Vai ler no ficheiro os users,listas e eleições ao inicializar o servidor
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void readFile () throws java.rmi.RemoteException{
        try {
            FileInputStream fi = new FileInputStream("myObjects.ser");
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            users = (CopyOnWriteArrayList<User>) oi.readObject();
            eleicoes = (CopyOnWriteArrayList<Eleicao>) oi.readObject();
            eleicoesVelhas = (CopyOnWriteArrayList<Eleicao>) oi.readObject();
            listas = (CopyOnWriteArrayList<Lista>) oi.readObject();

            oi.close();
            fi.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }  catch (ClassNotFoundException e) {
           // e.printStackTrace();
        }
    }

    /**
     * Vai percorrer todas as eleições e caso a data de fim tenha passado a data final remove das eleições atuais e
     * muda para eleições passadas
     * @throws java.rmi.RemoteException excepção que pode ocorrer na execução de uma remote call
     */
    public void realTimeEleicao() throws java.rmi.RemoteException{
        Date now = new Date();
        for (Eleicao eleicao : eleicoes) {
            if(eleicao.fim.before(now)){
                eleicoes.remove(eleicao);
                eleicoesVelhas.add(eleicao);
                if(Global.admin!=null) {
                    for (Notifications notifications: Global.admin) {
                        notifications.fimEleicao(eleicao.titulo, Global.rmiServer.getInfoEleicaoVelha(eleicoesVelhas.size() - 1));
                    }
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    public String registo(User user) throws java.rmi.RemoteException{
        for (User user1: users) {
            if(user1.equals(user)){
                return "User ja se encontra registado";
            }
        }
        users.add(user);
        return "Registado com sucesso";
    }

    /**
     * @inheritDoc
     */
    public boolean login(String user,String password, String CC) throws java.rmi.RemoteException{
        for(User userL : users){
            if(userL.password.equals(password) && userL.user.equals(user)){
                return userL.CC.equals(CC);
            }
        }
        return  false;
    }

    /**
     * @inheritDoc
     */
    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException   {
        eleicoes.add(eleicao);
        Lista lista = new Lista("Branco",eleicao.tipo);
        eleicao.addLista(lista);
        lista = new Lista("Nulo",eleicao.tipo);
        eleicao.addLista(lista);
    }

    /**
     * @inheritDoc
     */
    public String listaCandidatos(int n) throws java.rmi.RemoteException{
        Eleicao eleicao = eleicoes.get(n);
        StringBuilder s = new StringBuilder();
        int count = 1;
        for (Lista lista: eleicao.listas) {
            s.append(count).append("- ").append(lista.nome).append("\n");
            count+=1;
        }
        return s.toString();
    }


    /**
     * @inheritDoc
     */
    public String listarListas() throws java.rmi.RemoteException {
        StringBuilder listasS = new StringBuilder();
        int count = 1;
        for (Lista listaV : listas) {
            listasS.append(count);
            listasS.append("- ");
            listasS.append(listaV.nome);
            listasS.append("\n");
            count+=1;
        }
        return listasS.toString();
    }

    /**
     * @inheritDoc
     */
    public String listarEleicoes() throws java.rmi.RemoteException {
        StringBuilder votacoes = new StringBuilder();
        int count = 1;
        for (Eleicao eleicao : eleicoes) {
            votacoes.append(count);
            votacoes.append("- ");
            votacoes.append(eleicao.titulo);
            votacoes.append("\n");
            votacoes.append(eleicao.descricao);
            votacoes.append("\n");
            count+=1;
        }
        return votacoes.toString();
    }

    /**
     * @inheritDoc
     */
    public Eleicao getEleicao(int n) throws java.rmi.RemoteException {
        return eleicoes.get(n);
    }

    /**
     * @inheritDoc
     */
    public Lista getLista(int n) throws java.rmi.RemoteException {
        return listas.get(n);
    }


    /**
     * @inheritDoc
     */
    public String listarListasEleicao(int n) throws java.rmi.RemoteException {
        Eleicao eleicao =  eleicoes.get(n);
        StringBuilder sb = new StringBuilder();
        int counter = 1;
        for (Lista lista: eleicao.listas) {
            sb.append(counter).append("- ").append(lista.nome).append("\n");
            counter+=1;
        }
        return sb.toString();
    }

    /**
     * @inheritDoc
     */
    public String addListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException {
        Eleicao eleicao1=null;

        for (Eleicao eleicaoS: eleicoes) {
            if(eleicaoS.equals(eleicao)){
                eleicao1 = eleicaoS;
            }
        }
        if(lista.tipo.equals(eleicao1.tipo)) {
            eleicao1.addLista(lista);
            return "Lista adicionada com sucesso";
        }
        else{
            return "Lista e de tipo diferente da eleicao";
        }
    }

    /**
     * @inheritDoc
     */
    public void removeListaEleicao(Eleicao eleicao,int opcao)throws java.rmi.RemoteException {
        Eleicao eleicao1=null;

        for (Eleicao eleicaoS: eleicoes) {
            if(eleicaoS.equals(eleicao)){
                eleicao1 = eleicaoS;
            }
        }

        Lista lista = eleicao1.listas.get(opcao);
        eleicao1.removeLista(lista);
    }

    /**
     * @inheritDoc
     */
    public void removeLista(Lista lista)throws java.rmi.RemoteException {
        Lista lista1=null;

        for (Lista lista2: listas) {
            if(lista2.equals(lista)){
                lista1 = lista2;
            }
        }
        listas.remove(lista1);
    }

    /**
     * @inheritDoc
     */
    public void setTitulo(String titulo,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao1.setTitulo(titulo);
            }

        }
    }

    /**
     * @inheritDoc
     */
    public void setDescricao(String Descricao,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao1.setDescicao(Descricao);
            }

        }
    }

    /**
     * @inheritDoc
     */
    public void setDatas(Date dataI,Date dataf,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao1.setInicio(dataI);
                eleicao1.setFim(dataf);
            }

        }
    }

    /**
     * @inheritDoc
     */
    public void setTipo(String Tipo,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao1.setTipo(Tipo);
            }

        }
    }

    /**
     * @inheritDoc
     */
    public void addLista(Lista lista)throws java.rmi.RemoteException{
        listas.add(lista);
    }

    /**
     * @inheritDoc
     */
    public String addUserList(User user,Lista lista) throws java.rmi.RemoteException{
        Lista lista1=null;

        for (Lista lista2: listas) {
            if(lista2.equals(lista)){
                lista1 = lista2;
                break;
            }
        }

        if(!user.tipo.equals(lista1.tipo)){
            return "Utilizador nao tem a mesma funcao da lista";
        }
        else {
            for (User user1: lista1.lista) {
                if(user.equals(user1)){
                    return "User ja se encontra na lista";
                }
            }
            lista1.addUser(user);
            return "Utilizador adicionado com sucesso";
        }
    }

    /**
     * @inheritDoc
     */
    public void removeUserList(Lista lista,User user) throws java.rmi.RemoteException{
        Lista lista1=null;

        for (Lista lista2: listas) {
            if(lista2.equals(lista)){
                lista1 = lista2;
            }
        }

        User user1 = null;
        for (User user2: lista1.lista) {
            if(user2.equals(user)){
                user1 = user2;
            }
        }

        lista1.removeUser(user1);
    }

    /**
     * @inheritDoc
     */
    public String getInfoEleicaoVelha(int pos)throws java.rmi.RemoteException{
        StringBuilder info = new StringBuilder();
        Eleicao eleicao = eleicoesVelhas.get(pos);
        ArrayList<Lista> listas = eleicao.listas;
        ArrayList<Integer> votos = eleicao.votosDone;
        Map<Lista,Integer> votacaoLista = new LinkedHashMap<>();
        int totalVotos = 0;
        for (int i = 0; i < votos.size(); i++) {
            votacaoLista.put(listas.get(i),votos.get(i));
            totalVotos+=votos.get(i);
        }

        Set<Map.Entry<Lista, Integer>> companyFounderSet = votacaoLista.entrySet();

        // 2. convert LinkedHashMap to List of Map.Entry
        List<Map.Entry<Lista,Integer>> companyFounderListEntry =
                new ArrayList<Map.Entry<Lista,Integer>>(companyFounderSet);

        // 3. sort list of entries using Collections class'
        // utility method sort(ls, cmptr)
        Collections.sort(companyFounderListEntry,
                new Comparator<Map.Entry<Lista, Integer>>() {

                    @Override
                    public int compare(Map.Entry<Lista,Integer> es1,
                                       Map.Entry<Lista, Integer> es2) {
                        return es2.getValue().compareTo(es1.getValue());
                    }
                });

        // 4. clear original LinkedHashMap
        votacaoLista.clear();

        // 5. iterating list and storing in LinkedHahsMap
        for(Map.Entry<Lista, Integer> map : companyFounderListEntry){
            votacaoLista.put(map.getKey(), map.getValue());
        }



        int count = 1;
        if(totalVotos==0){
            totalVotos+=1;
        }
        for (Map.Entry<Lista,Integer> entry : votacaoLista.entrySet()) {
            String nome = entry.getKey().nome;
            int numVoto = entry.getValue();
            info.append(count).append("-").append(nome).append(" Votos-").append(numVoto).append(" Percentagem-")
                    .append((float) numVoto/totalVotos*100).append("%").append("\n");
            count+=1;
        }
        for (User user: users) {
            for (Map.Entry<Eleicao,String> entry : user.localVoto.entrySet()) {
                if (entry.getKey().equals(eleicao)){
                    info.append(user.user).append("- ").append(entry.getValue()).append("\n");
                }
            }
        }
        return info.toString();
    }

    /**
     * @inheritDoc
     */
    public String getEleicoesVelhas() throws  java.rmi.RemoteException{
        StringBuilder votacoes = new StringBuilder();
        int counter = 1;
        for (Eleicao eleicao:eleicoesVelhas) {
            votacoes.append(counter).append("- ").append(eleicao.titulo).append("\n");
            counter+=1;
        }
        return  votacoes.toString();
    }

    /**
     * @inheritDoc
     */
    public String printUsers() throws java.rmi.RemoteException {
        StringBuilder votacoes = new StringBuilder();
        int count = 1;
        for (User user: users) {
            votacoes.append(count);
            votacoes.append("- ");
            votacoes.append(user.user);
            votacoes.append("\n");
            count+=1;
        }
        return votacoes.toString();

    }

    /**
     * @inheritDoc
     */
    public User getUser(int pos) throws java.rmi.RemoteException {
        return users.get(pos);
    }

    /**
     * @inheritDoc
     */
    public boolean identificarLeitor(String CC) throws java.rmi.RemoteException{
        for(User userL : users){
            if(userL.CC.equals(CC)){
                return true;
            }
        }
        return  false;
    }

    /**
     * @inheritDoc
     */
    public String votar(int opcao,String CC,int nEleicao,DepMesa mesa) throws java.rmi.RemoteException {
        User user = null;
        for (User userS:users) {
            if(userS.CC.equals(CC)){
                user = userS;
                break;
            }
        }
        Eleicao eleicao = eleicoes.get(nEleicao);

        Date date = new Date();

        if(eleicao.inicio.after(date)){
            return "A votacao nesta eleicao ainda nao comecou";
        }

        int flag=0;
        for (DepMesa mesaEleicao: eleicao.maquinasVotacao) {
            if(mesaEleicao.equals(mesa)){
                flag=1;
                break;
            }
        }

        if(flag==0){
            return "Esta mesa nao se encontra registada para esta eleicao";
        }

        for (Map.Entry<Eleicao, String> entry : user.localVoto.entrySet()) {
            if (entry.getKey().equals(eleicao)){
                return "Utilizador ja votou nesta eleicao!";
            }
        }


        if(user.departamento.equals(mesa.departamento)){
            if(user.tipo.equals(eleicao.tipo)){
                user.addVoto(eleicao,mesa.departamento+" "+date.toString());
                eleicao.addVoto(opcao);
                return "Voto com sucesso";
            }
            else{
                return "User nao pode votar nesta eleicao, e para uma funcao diferente!";
            }
        }
        else{
            return "O utilizador nao pode votar nesta mesa";
        }
    }

    /**
     * @inheritDoc
     */
    public String addMesa(DepMesa mesa) throws java.rmi.RemoteException{
        for (DepMesa mesa1: mesasVoto) {
            if(mesa1.departamento.equals(mesa.departamento)){
                return "Ja se encontra uma mesa neste departamento";
            }
        }
        for (Notifications notifications: Global.admin) {
            notifications.estadoMesa("On",mesa.departamento,mesa.id);
        }
        mesasVoto.add(mesa);
        return "";
    }

    /**
     * @inheritDoc
     */
    public void removeMesa(DepMesa mesa) throws java.rmi.RemoteException{
        for (Notifications notifications: Global.admin) {
            notifications.estadoMesa("Off",mesa.departamento,mesa.id);
        }
        for (DepMesa mesa1: mesasVoto) {
            if(mesa1.equals(mesa)){
                mesasVoto.remove(mesa1);
                break;
            }
        }
        for (Eleicao eleicao: eleicoes) {
            int counter = 0;
            for (DepMesa mesa1: eleicao.maquinasVotacao) {
                if(mesa1.equals(mesa)){
                    eleicao.removeMaquina(counter);
                    break;
                }
                counter+=1;
            }
        }
    }

    /**
     * @inheritDoc
     */
    public int numVotantes(int opcao) throws java.rmi.RemoteException{
        Eleicao eleicao = eleicoes.get(opcao);
        int soma = 0;
        for (User user: users) {
            for (Map.Entry<Eleicao,String> entry : user.localVoto.entrySet()) {
                if (entry.getKey().equals(eleicao)){
                    soma+=1;
                }
            }
        }
        return soma;
    }

    /**
     * @inheritDoc
     */
    public String addMesaEleicao(DepMesa Mesa, Eleicao eleicao) throws  java.rmi.RemoteException{
        DepMesa mesa1=null;
        Eleicao eleicao1=null;
        for (DepMesa mesa: mesasVoto) {
            if(mesa.equals(Mesa)){
                mesa1=mesa;
                break;
            }
        }
        for (Eleicao eleicao2: eleicoes) {
            if(eleicao.equals(eleicao2)){
                eleicao1=eleicao2;
            }
        }
        for (DepMesa mesa: eleicao1.maquinasVotacao){
            if(mesa1.departamento.equals(mesa.departamento)){
                return "Ja existe uma mesa de voto para essa eleicao";
            }
        }
        eleicao1.maquinasVotacao.add(mesa1);
        return "Mesa adicionada com sucesso";
    }

    /**
     * @inheritDoc
     */
    public void removeMesaEleicao(int opcao, Eleicao eleicao) throws  java.rmi.RemoteException{
        Eleicao eleicao1=null;
        for (Eleicao eleicao2: eleicoes) {
            if(eleicao.equals(eleicao2)){
                eleicao1=eleicao2;
            }
        }

        eleicao1.removeMaquina(opcao);
    }

    /**
     * @inheritDoc
     */
    public String listaMaquina() throws java.rmi.RemoteException{
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;
        for (DepMesa mesa: mesasVoto) {
            stringBuilder.append(count).append(" -").append(mesa.id).append("- ").append(mesa.departamento).append("\n");
            count+=1;
        }
        return stringBuilder.toString();
    }

    /**
     * @inheritDoc
     */
    public DepMesa getMesa(int opcao) throws java.rmi.RemoteException{
        return mesasVoto.get(opcao);
    }
    // ========================================================= Ler config
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

        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            assert fis != null;
            fis.close();
        }

        return prop;
    }

    /**
     * Le as configurações do ficheiro de config
     */
    public void readPorto() throws IOException {

        Properties prop = readPropertiesFile("config.properties");
        String portoInString = prop.getProperty("portoRMI");
        portoRMI = Integer.parseInt(portoInString);
        String portoUDPSring = prop.getProperty("portoUDP");
        Global.portoUDP = Integer.parseInt(portoUDPSring);
        serverAddress =  prop.getProperty("RMIAddress");

    }

    // ======================================================== Main

    /**
     * Main da função onde vai ser criada as threads para o UDP Primario/UDP Secundário. O 1º RMI a ser ligado cria a
     * thread Primario e fica a espera de receber mensagem pelo seundario. Apos o Servidor Primario morrer o Secundario
     * continua o seu trabalho
     */
    public static void main(String args[]) throws IOException {
        String a;
        Global.rmiServer.readPorto();

        System.getProperties().put("java.security.policy", "policy.all");
        System.setProperty("java.rmi.server.hostname", serverAddress);
        System.setSecurityManager(new RMISecurityManager());

        DatagramSocket aSocket = null;
        boolean primario = false;


        try{
            aSocket = new DatagramSocket(6789);
            System.out.println("Sou primario");
            primario = true;
        }catch (SocketException e){
            System.out.println("Sou Secundario");

        }
        try {
            if(primario) {
                new RealTimeUpdate();
                new UDPPrim(aSocket);

                Global.rmiServer.readFile();
                Registry r = LocateRegistry.createRegistry(Global.rmiServer.portoRMI);
                r.rebind("votacao", Global.rmiServer);
                System.out.println("Hello Server ready.");
                while (true) {

                }
            }
            else{
                new UDPSec();
                while (true){
                    if(Global.prim){
                        new RealTimeUpdate();
                        aSocket = new DatagramSocket(6789);
                        new UDPPrim(aSocket);
                        Global.rmiServer.readFile();
                        Registry r = LocateRegistry.createRegistry(Global.rmiServer.portoRMI);
                        r.rebind("votacao", Global.rmiServer);
                        System.out.println("Hello Server ready.");
                        while (true) {
                        }
                    }
                }
            }
        } catch (Exception re) {
            System.out.println("Exception in HelloServer.main: " + re);
        }
    }
}
