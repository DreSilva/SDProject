import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Lista implements Serializable {
    ArrayList<User> lista = new ArrayList<>();
    String nome;

    public Lista(String nome){this.nome=nome;}

    public void addUser(User user){
        lista.add(user);
    }

    public void removeUser(User user){
        lista.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lista lista1 = (Lista) o;
        return lista.equals(lista1.lista) && nome.equals(lista1.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lista, nome);
    }
}
