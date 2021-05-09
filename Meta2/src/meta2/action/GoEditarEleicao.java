package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GoEditarEleicao extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> tipos;
    private String tipo,titulo,descricao,horaI,horaF,eleicao;
    private Date dataInicial,dataFinal;


    @Override
    public String execute() throws Exception {
        this.getHeyBean().setEleicao(eleicao);
        tipos = new ArrayList<radioOptions>();
        tipos.add(new radioOptions("Estudante", "Estudante"));
        tipos.add(new radioOptions("Docente", "Docente"));
        tipos.add(new radioOptions("Funcionario", "Funcionario"));
        ArrayList<String> s = this.getHeyBean().getEleicaoInfo();
        titulo = s.get(0);
        descricao = s.get(1);
        tipo = s.get(2);
        String aux = s.get(3);
        String[] aux2 = aux.split(" ");
        dataInicial = new SimpleDateFormat("dd-MM-yyyy").parse(aux2[0]);
        horaI = aux2[1];
        aux = s.get(4);
        aux2 = aux.split(" ");
        dataFinal = new SimpleDateFormat("dd-MM-yyyy").parse(aux2[0]);
        horaF = aux2[1];
        return SUCCESS;
    }

    public List<radioOptions> getTipos() {
        return tipos;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getHoraF() {
        return horaF;
    }

    public String getTipo() {
        return tipo;
    }

    public String getHoraI() {
        return horaI;
    }

    public void setEleicao(String eleicao) {
        this.eleicao = eleicao;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
