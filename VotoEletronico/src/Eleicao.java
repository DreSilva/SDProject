import java.io.Serializable;
import java.util.Date;

public class Eleicao implements Serializable {
    Date inicio,fim;
    String titulo,descicao,nucelo,tipo;
    int votos;
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
}
