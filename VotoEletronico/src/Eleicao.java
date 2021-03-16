import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Eleicao implements Serializable {
    Date inicio,fim;
    String titulo,descicao,nucleo,tipo;
    int votos;
    ArrayList<DepMesa> maquinasVotacao = new ArrayList<>();
    ArrayList<Lista> listas = new ArrayList<>();
    ArrayList<Integer> votosDone = new ArrayList<>();
    public Eleicao(Date inicio, Date fim, String titulo, String descricao, String nucleo, String tipo){
        this.inicio=inicio;
        this.fim=fim;
        this.titulo=titulo;
        this.descicao=descricao;
        this.nucleo=nucleo;
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

    public void setNucleo(String nucleo) {
        this.nucleo = nucleo;
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
}
