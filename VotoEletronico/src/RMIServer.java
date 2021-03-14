import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

class TempoEleicoes{

}

public class RMIServer extends UnicastRemoteObject implements Voto {
    static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Eleicao> eleicoes = new ArrayList<>();

    public RMIServer() throws RemoteException {
        super();
    }

    public void registo(User user) throws java.rmi.RemoteException{
        users.add(user);
    }

    public boolean login(String user,String password, String CC) throws java.rmi.RemoteException{
        for(User userL : users){
            if(userL.password.equals(password) && userL.user.equals(user)){
                if(userL.CC.equals(CC)){
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return  false;
    }

    public void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException{
        eleicoes.add(eleicao);
    }

    public void gerirMesas(DepMesa cliente, String opcao, Eleicao eleicao) throws RemoteException{
        if(opcao.equals("Remover")){
                eleicao.removeMaquina(cliente);
        }
        else{
            eleicao.addMaquina(cliente);
        }
    }


    public String listarVotacoes() throws java.rmi.RemoteException{
        StringBuilder votacoes = new StringBuilder();
        for (Eleicao eleicao : eleicoes){
            votacoes.append(eleicao.titulo);
            votacoes.append(eleicao.titulo);
            votacoes.append(eleicao.descicao);
            for (Lista lista : eleicao.listas){
                votacoes.append(lista.nome);
                for(User user: lista.lista){
                    votacoes.append(user.user);
                }
            }
        }
        return votacoes.toString();
    }

    public boolean identificarLeitor(String CC) throws java.rmi.RemoteException{
        for(User userL : users){
            if(userL.password.equals(CC)){
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
