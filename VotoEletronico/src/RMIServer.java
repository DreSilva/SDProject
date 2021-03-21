import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

class Global{
   static volatile boolean prim = false;
   static volatile RMIServer rmiServer;
   static volatile AdminConsole admin;

    static {
        try {
            rmiServer = new RMIServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

class UDPPrim extends Thread{
    DatagramSocket aSocket;
    public UDPPrim(DatagramSocket aSocket){
        this.aSocket=aSocket;
        this.start();
    }

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
            e.printStackTrace();
        } finally {if(aSocket != null) aSocket.close();}
    }
}

class UDPSec extends Thread{
    public UDPSec(){this.start();}

    @Override
    public void run() {
        try (DatagramSocket aSocket = new DatagramSocket()) {

            String texto = "";
            InputStreamReader input = new InputStreamReader(System.in);
            aSocket.setSoTimeout(3000);

            while (true) {
                texto = "ping";

                byte[] m = texto.getBytes();
                InetAddress aHost = InetAddress.getByName("localhost");
                int serverPort = 6789;
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
            e.printStackTrace();
        }
    }

}

class RealTimeUpdate extends Thread{
    public RealTimeUpdate(){this.start();}

    @Override
    public void run() {
        try {
            Global.rmiServer.realTimeEleicao();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

public class RMIServer extends UnicastRemoteObject implements Voto {
    static CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Eleicao> eleicoes = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Eleicao> eleicoesVelhas = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Lista> listas = new CopyOnWriteArrayList<>();


    public RMIServer() throws RemoteException {
        super();
    }

    public void subscribeAdmin(AdminConsole nAdmin) throws RemoteException {
        Global.admin = nAdmin;
    }

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void realTimeEleicao() throws java.rmi.RemoteException{
        Date now = new Date();
        for (Eleicao eleicao : eleicoes) {
            if(eleicao.fim.before(now)){
                eleicoes.remove(eleicao);
                eleicoesVelhas.add(eleicao);
                Global.admin.fimEleicao(eleicao.titulo,Global.rmiServer.getInfoEleicaoVelha(eleicoesVelhas.size()-1));
            }
        }
    }

    public void registo(User user) throws java.rmi.RemoteException{
        users.add(user);
    }

    public boolean login(String user,String password, String CC) throws java.rmi.RemoteException{
        for(User userL : users){
            if(userL.password.equals(password) && userL.user.equals(user)){
                return userL.CC.equals(CC);
            }
        }
        return  false;
    }

    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException   {
        eleicoes.add(eleicao);
        Lista lista = new Lista("Branco");
        eleicao.addLista(lista);
        lista = new Lista("Nulo");
        eleicao.addLista(lista);
    }

    public void gerirMesas(DepMesa cliente, String opcao, Eleicao eleicao) throws RemoteException{ // provavelmente vai ser preciso mudar
        if(opcao.equals("Remover")){
                eleicao.removeMaquina(cliente);
        }
        else{
            eleicao.addMaquina(cliente);
        }
    }

    public String listaCandidatos(int n) throws java.rmi.RemoteException{
        Eleicao eleicao = eleicoes.get(n);
        StringBuilder s = new StringBuilder();
        int count = 1;
        for (Lista lista: eleicao.listas) {
            s.append(count).append("- ").append(lista.nome).append("\n");
        }
        return s.toString();
    }

    public void criarLista(Lista lista) throws java.rmi.RemoteException{
        listas.add(lista);
    }

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

    public Eleicao getEleicao(int n) throws java.rmi.RemoteException {
        return eleicoes.get(n);
    }

    public Lista getLista(int n) throws java.rmi.RemoteException {
        return listas.get(n);
    }

    public void addListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException { // provavelmente vai ser preciso mudar
        eleicao.addLista(lista);
    }

    public void removeListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException { // provavelmente vai ser preciso mudar
        eleicao.addLista(lista);
    }

    public void removeLista(Lista lista)throws java.rmi.RemoteException { //provavelmente vai ser preciso mudar
        listas.remove(lista);
    }

    public void setTitulo(String titulo,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao1.setTitulo(titulo);
            }

        }
    }

    public void setDescricao(String Descricao,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao1.setDescicao(Descricao);
            }

        }
    }

    public void setDatas(Date dataI,Date dataf,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao.setInicio(dataI);
                eleicao.setFim(dataf);
            }

        }
    }

    public void setTipo(String Tipo,Eleicao eleicao) throws java.rmi.RemoteException{
        for (Eleicao eleicao1: eleicoes) {
            if(eleicao.equals(eleicao1)){
                eleicao.setTipo(Tipo);
            }

        }
    }

    public void addLista(Lista lista)throws java.rmi.RemoteException{
        listas.add(lista);
    }

    public void addUserList(User user,Lista lista) throws java.rmi.RemoteException{ // provavelmente vai ser preciso mudar
        lista.addUser(user);
    }

    public void removeEleicao(Eleicao eleicao) throws java.rmi.RemoteException{ //provavelmente vai ser preciso mudar
        eleicoes.remove(eleicao);
    }

    public void removeUserList(Lista lista,User user) throws java.rmi.RemoteException{//provavelmente vai ser preciso mudar
        lista.removeUser(user);
    }

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
        int count = 1;
        for (Map.Entry<Lista,Integer> entry : votacaoLista.entrySet()) {
            String nome = entry.getKey().nome;
            int numVoto = entry.getValue();
            info.append(count).append("º-").append(nome).append(" Votos-").append(numVoto).append(" Percentagem-")
                    .append(numVoto/totalVotos).append("\n");
        }
        return info.toString();
    }

    public String getEleicoesVelhas() throws  java.rmi.RemoteException{
        StringBuilder votacoes = new StringBuilder();
        int counter = 1;
        for (Eleicao eleicao:eleicoesVelhas) {
            votacoes.append(counter).append("- ").append(eleicao.titulo).append("\n");
            counter+=1;
        }
        return  votacoes.toString();
    }

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

    public User getUser(int pos) throws java.rmi.RemoteException {
        return users.get(pos);
    }

    public String listarVotacoes() throws java.rmi.RemoteException{
        StringBuilder votacoes = new StringBuilder();
        for (Eleicao eleicao : eleicoes){
            votacoes.append(eleicao.titulo);
            votacoes.append("\n");
            votacoes.append(eleicao.descricao);
            for (Lista lista : eleicao.listas){
                votacoes.append(lista.nome);
                votacoes.append("\n");
                for(User user: lista.lista){
                    votacoes.append(user.user);
                    votacoes.append("\n");
                }
            }
        }
        return votacoes.toString();
    }

    public boolean identificarLeitor(String CC) throws java.rmi.RemoteException{
        for(User userL : users){
            if(userL.CC.equals(CC)){
                return true;
            }
        }
        return  false;
    }

    public String votar(int opcao,String CC,int nEleicao,DepMesa mesa) throws java.rmi.RemoteException { //provavelmente vai ser preciso mudar
        User user = null;
        for (User userS:users) {
            if(userS.CC.equals(CC)){
                user = userS;
                break;
            }
        }
        Eleicao eleicao = eleicoes.get(nEleicao);
        user.addVoto(eleicao,mesa.departamento);
        eleicao.addVoto(opcao);
        return "Andre para de ser fking gay seu fking tard";
    }

    // =======================================================

    public static void main(String args[]) {
        String a;

        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());

        InputStreamReader input = new InputStreamReader(System.in);

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
                Registry r = LocateRegistry.createRegistry(7000);
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
                        Registry r = LocateRegistry.createRegistry(7000);
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
