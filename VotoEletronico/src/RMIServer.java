import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

class UDPTalk extends Thread{

}


public class RMIServer extends UnicastRemoteObject implements Voto {
    static CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Eleicao> eleicoes = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Eleicao> eleicoesVelhas = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<Lista> listas = new CopyOnWriteArrayList<>();

    public RMIServer() throws RemoteException {
        super();
    }

    public void writeFile() throws java.rmi.RemoteException{
        try {
            FileOutputStream f = new FileOutputStream("myObjects.txt");
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
            FileInputStream fi = new FileInputStream("myObjects.txt");
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

    public void gerirMesas(DepMesa cliente, String opcao, Eleicao eleicao) throws RemoteException{
        if(opcao.equals("Remover")){
                eleicao.removeMaquina(cliente);
        }
        else{
            eleicao.addMaquina(cliente);
        }
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
            votacoes.append(eleicao.descicao);
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

    public void addListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException {
        eleicao.addLista(lista);
    }

    public void removeListaEleicao(Eleicao eleicao,Lista lista)throws java.rmi.RemoteException {
        eleicao.addLista(lista);
    }

    public void removeLista(Lista lista)throws java.rmi.RemoteException {
        listas.remove(lista);
    }

    public void addLista(Lista lista)throws java.rmi.RemoteException{
        listas.add(lista);
    }

    public void addUserList(User user,Lista lista) throws java.rmi.RemoteException{
        lista.addUser(user);
    }

    public void removeEleicao(Eleicao eleicao) throws java.rmi.RemoteException{
        eleicoes.remove(eleicao);
    }

    public void removeUserList(Lista lista,User user) throws java.rmi.RemoteException{
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
        Date now = new Date();
        for (Eleicao eleicao : eleicoes) {
            if(eleicao.fim.before(now)){
                eleicoes.remove(eleicao);
                eleicoesVelhas.add(eleicao);
            }
        }

        StringBuilder votacoes = new StringBuilder();
        int counter = 1;
        for (Eleicao eleicao:eleicoesVelhas) {
            votacoes.append(counter).append("- ").append(eleicao.titulo).append("\n");
            counter+=1;
        }
        return  votacoes.toString();
    }

    public String printUsers() throws java.rmi.RemoteException { //not anymore hehe
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
            votacoes.append(eleicao.descicao);
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

    public void votar(int opcao,User user,Eleicao eleicao,DepMesa mesa) throws java.rmi.RemoteException {
        user.addVoto(eleicao,mesa.departamento);
        eleicao.addVoto(opcao);
    }

    // =======================================================

    public static void main(String args[]) {
        String a;

        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        try {
            //User user = new User();
            RMIServer h = new RMIServer();
            h.readFile();
            Registry r = LocateRegistry.createRegistry(7000);
            r.rebind("votacao", h);
            System.out.println("Hello Server ready.");
            while (true) {
                //System.out.print("> ");
                //a = reader.readLine();
                //for(Hello_C_I  client : listClient){
                    //client.print_on_client(a);
                //}

            }
        } catch (Exception re) {
            System.out.println("Exception in HelloServer.main: " + re);
        }
    }
}
