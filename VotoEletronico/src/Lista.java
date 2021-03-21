import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Classe para cada Lista de eleição
 */
public class Lista implements Serializable {
    ArrayList<User> lista = new ArrayList<>();
    String nome;

    /**
     * Objeto lista com determinado nome
     * @param nome Nome da lista criada
     */
    public Lista(String nome){this.nome=nome;}

    /**
     * Adicionar elemento à lista
     * @param user utilizador a adicionar a lista
     */
    public void addUser(User user){
        lista.add(user);
    }

    /**
     * Remover elemento à lista
     * @param user utilizador a remover da lista
     */
    public void removeUser(User user){
        lista.remove(user);
    }

    /**
     * Compara listas
     * @param o Lista a comparar
     * @return True se for igual False se nao for
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lista lista1 = (Lista) o;
        return lista.equals(lista1.lista) && nome.equals(lista1.nome);
    }

}
