import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class RMIServer extends UnicastRemoteObject implements Voto {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Eleicao> eleicoes = new ArrayList<>();
    ArrayList<Lista> listas = new ArrayList<>();

    public RMIServer() throws RemoteException {
        super();
    }

    public void registo(User user){
        users.add(user);
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
            r.rebind("XPTO", h);
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
