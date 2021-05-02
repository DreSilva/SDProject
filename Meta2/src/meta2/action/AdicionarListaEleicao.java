package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdicionarListaEleicao extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> listas;
    private List<radioOptions> eleicoes;
    private String eleicao,lista;

    //TODO POR A LISTA NA ELEICAO E VERIFICAR SE ELA NAO ESTA JA LA

    @Override
    public void validate() {
        if(listas==null){
            listas = new ArrayList<>();
            int counter = 0;
            ArrayList<String> s = null;
            try {
                s = this.getHeyBean().getEleicao();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s) {
                listas.add(new radioOptions(String.valueOf(counter),a));
            }
        }
        if(eleicoes==null){
            int counter = 0;
            ArrayList<String> s2 = null;
            eleicoes = new ArrayList<>();
            try {
                s2 = this.getHeyBean().getListas();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s2) {
                eleicoes.add(new radioOptions(String.valueOf(counter),a));
            }
        }


    }

    @Override
    public String execute() throws RemoteException {
        this.getHeyBean().setLista(lista);
        this.getHeyBean().setEleicao(eleicao);
        return SUCCESS;
    }

    public void setEleicao(String eleicao) {
        this.eleicao = eleicao;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public HeyBean getHeyBean(){
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
