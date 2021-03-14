import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;

public class AdminConsole {
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException{
        System.out.println("Simle example of use of a remote calculator");
        Voto ci = (Voto) Naming.lookup("votacao");
        Date date = new Date(System.currentTimeMillis());
        User user = new User("Joao","andrefeio","dei", "fctuc","915918995",
                "Estudante","Nao","123455668989" ,date);
        ci.registo(user);
        ci.login("Joao","andrefeio","123455668989");


    }
}
