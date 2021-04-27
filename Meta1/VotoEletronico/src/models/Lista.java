package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe para cada models.Lista de eleição
 */
public class Lista implements Serializable {
    public ArrayList<User> lista = new ArrayList<>();
    public String nome;
    public String tipo;
    public long idofList;

    /**
     * Objeto lista com determinado nome
     * @param nome Nome da lista criada
     */
    public Lista(String nome,String tipo){
        this.nome=nome;
        Date date = new Date();
        this.idofList = date.getTime();
        this.tipo = tipo;
    }

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
     * @param o models.Lista a comparar
     * @return True se for igual False se nao for
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lista lista1 = (Lista) o;
        return this.idofList == lista1.idofList;
    }

}
