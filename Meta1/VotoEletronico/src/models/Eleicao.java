package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe com todos os atributos necessários para uma eleição
 */
public class Eleicao implements Serializable {
    public Date inicio,fim;
    public String titulo,descricao,tipo;
    public int votos;
    public ArrayList<DepMesa> maquinasVotacao = new ArrayList<>();
    public ArrayList<Lista> listas = new ArrayList<>();
    public ArrayList<Integer> votosDone = new ArrayList<>();

    /**
     * Objeto para uma elição
     * @param inicio Data de inicio da eleição
     * @param fim Data de fim da eleição
     * @param titulo Titulo da Eleição
     * @param descricao Descrição da Eleição
     * @param tipo Tipo da eleição, se é para estudates,docentes ou docentes
     */
    public Eleicao(Date inicio, Date fim, String titulo, String descricao, String tipo){
        this.inicio=inicio;
        this.fim=fim;
        this.titulo=titulo;
        this.descricao=descricao;
        this.tipo=tipo;
        this.votos = 0;
    }

    /**
     * Muda data de inicio da eleição
     * @param inicio Nova data de Inicio
     */
    public void setInicio(Date inicio){
        this.inicio=inicio;
    };

    /**
     * Muda descrição da eleição
     * @param descicao Nova Descrição da eleição
     */
    public void setDescicao(String descicao) {
        this.descricao = descicao;
    }

    /**
     * Muda data de fim da eleição
     * @param fim Nova data de fim
     */
    public void setFim(Date fim) {
        this.fim = fim;
    }

    /**
     * Muda o tipo da eleição
     * @param tipo Novo tipo de eleição
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Muda o titulo da eleição
     * @param titulo novo titulo da eleição
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Adiciona uma lista para votar na eleição
     * @param lista models.Lista a adicionar
     */
    public void addLista(Lista lista){
        listas.add(lista);
        votosDone.add(0);
    }

    /**
     * Remove uma lista para votar na eleição
     * @param lista models.Lista a Remover
     */
    public void removeLista(Lista lista){
        int pos = listas.indexOf(lista);
        votosDone.remove(pos);
        listas.remove(lista);
    }

    /**
     * Adiciona mesa de eleição
     * @param maq Mesa a adicionar
     */
    public void addMaquina(DepMesa maq){
        maquinasVotacao.add(maq);
    }

    /**
     * Remover mesa de eleição
     * @param opcao Mesa a Remover
     */
    public void removeMaquina(int opcao){
        maquinasVotacao.remove(opcao);
    }

    /**
     * Adicionar voto a uma lista
     * @param opcao lista á qual se adiciona um voto
     */
    public void addVoto(int opcao){
        votosDone.set(opcao, votosDone.get(opcao)+1);
    }

    /**
     * Verifica se as eleições passadas são iguais ou n
     * @param o objeto a comparar
     * @return True se forem False se não forem
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Eleicao eleicao = (Eleicao) o;


        if (votos != eleicao.votos) return false;

        if (inicio != null ? !inicio.equals(eleicao.inicio) : eleicao.inicio != null) return false;

        if (fim != null ? !fim.equals(eleicao.fim) : eleicao.fim != null) return false;

        if (titulo != null ? !titulo.equals(eleicao.titulo) : eleicao.titulo != null) return false;

        if (descricao != null ? !descricao.equals(eleicao.descricao) : eleicao.descricao != null) return false;

        return (tipo.equals(eleicao.tipo));
    }

}
