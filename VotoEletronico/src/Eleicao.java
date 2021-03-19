import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Eleicao implements Serializable {
    Date inicio,fim;
    String titulo,descicao,tipo;
    int votos;
    ArrayList<DepMesa> maquinasVotacao = new ArrayList<>();
    ArrayList<Lista> listas = new ArrayList<>();
    ArrayList<Integer> votosDone = new ArrayList<>();
    public Eleicao(Date inicio, Date fim, String titulo, String descricao, String tipo){
        this.inicio=inicio;
        this.fim=fim;
        this.titulo=titulo;
        this.descicao=descricao;
        this.tipo=tipo;
        this.votos = 0;
    }

    public void setInicio(Date inicio){
        this.inicio=inicio;
    };

    public void setDescicao(String descicao) {
        this.descicao = descicao;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }


    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void addLista(Lista lista){
        listas.add(lista);
        votosDone.add(0);
    }

    public void removeLista(Lista lista){
        int pos = listas.indexOf(lista);
        votosDone.remove(pos);
        listas.remove(lista);
    }

    public void addMaquina(DepMesa maq){
        maquinasVotacao.add(maq);
    }

    public void removeMaquina(DepMesa maq){
        maquinasVotacao.remove(maq);
    }

    public void addVoto(int opcao){
        votosDone.set(opcao, votosDone.get(opcao)+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Eleicao eleicao = (Eleicao) o;


        if (votos != eleicao.votos) return false;

        if (inicio != null ? !inicio.equals(eleicao.inicio) : eleicao.inicio != null) return false;

        if (fim != null ? !fim.equals(eleicao.fim) : eleicao.fim != null) return false;

        if (titulo != null ? !titulo.equals(eleicao.titulo) : eleicao.titulo != null) return false;

        if (descicao != null ? !descicao.equals(eleicao.descicao) : eleicao.descicao != null) return false;

        return (tipo.equals(eleicao.tipo));
    }

    @Override
    public int hashCode() {
        int result = inicio != null ? inicio.hashCode() : 0;
        result = 31 * result + (fim != null ? fim.hashCode() : 0);
        result = 31 * result + (titulo != null ? titulo.hashCode() : 0);
        result = 31 * result + (descicao != null ? descicao.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + votos;
        result = 31 * result + (maquinasVotacao != null ? maquinasVotacao.hashCode() : 0);
        result = 31 * result + (listas != null ? listas.hashCode() : 0);
        result = 31 * result + (votosDone != null ? votosDone.hashCode() : 0);
        return result;
    }
}
