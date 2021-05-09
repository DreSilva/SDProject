package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CriarLista extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> tipos;
    private String tipo,nome;

    @Override
    public void validate() {
        if(tipos==null){
            tipos = new ArrayList<radioOptions>();
            tipos.add( new radioOptions("Estudante", "Estudante") );
            tipos.add( new radioOptions("Docente", "Docente") );
            tipos.add( new radioOptions("Funcionario", "Funcionario") );
        }
        if(tipo==null){
            addFieldError("tError", "Tipo da Lista é Obrigatorio.");
        }
        if(nome.equals("")){
            addFieldError("tipo", "Nome da Lista é obrigatorio.");
        }
    }

    @Override
    public String execute() throws RemoteException {
        this.getHeyBean().setTipo(tipo);
        this.getHeyBean().setNome(nome);
        this.getHeyBean().criarLista();
        return SUCCESS;
    }

    public List<radioOptions> getTipos() {
        return tipos;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
