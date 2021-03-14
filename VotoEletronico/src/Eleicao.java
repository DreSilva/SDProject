import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Eleicao implements Serializable {
    Date inicio,fim;
    String titulo,descicao,nucelo,tipo;
    int votos;
    ArrayList<DepMesa> maquinasVotacao = new ArrayList<>();
    ArrayList<Lista> listas = new ArrayList<>();
    ArrayList<Integer> votosDone = new ArrayList<>();
    public Eleicao(Date inicio, Date fim, String titulo, String descricao, String nucleo, String tipo){
        this.inicio=inicio;
        this.fim=fim;
        this.titulo=titulo;
        this.descicao=descricao;
        this.nucelo=nucleo;
        this.tipo=tipo;
        this.votos = 0;
    }

    public void editarEleicao(){

    };

    public void addLista(Lista lista){
        listas.add(lista);
        votosDone.add(0);
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
