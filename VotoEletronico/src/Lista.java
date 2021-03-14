import java.io.Serializable;
import java.util.ArrayList;

public class Lista implements Serializable {
    ArrayList<User> lista = new ArrayList<>();
    String nome;

    public Lista(){}

    public void addUser(User user){
        lista.add(user);
    }

}
