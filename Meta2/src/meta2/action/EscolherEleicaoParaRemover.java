package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EscolherEleicaoParaRemover extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> eleicoes;
    private List<radioOptions> listas;
    private String eleicao;

    @Override
    public void validate() {
        if(eleicoes==null){
            eleicoes = new ArrayList<radioOptions>();
            int counter = 0;
            try {
                ArrayList<String> s = this.getHeyBean().getEleicao();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            ArrayList<String> s2 = null;
            try {
                s2 = this.getHeyBean().getListas();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s2) {
                eleicoes.add(new radioOptions(String.valueOf(counter),a));
                counter+=1;
            }
        }
        if(eleicao==null){
            addFieldError("tError", "Eleição é obrigatorio.");
        }
    }

    @Override
    public String execute() throws RemoteException {

        this.getHeyBean().setEleicao(eleicao);
        listas = new ArrayList<radioOptions>();
        int counter = 0;
        ArrayList<String> s2 = this.getHeyBean().getListasEleicao();
        for (String a: s2) {
            listas.add(new radioOptions(String.valueOf(counter),a));
            counter+=1;
        }
        return SUCCESS;
    }


    public List<radioOptions> getEleicoes() {
        return eleicoes;
    }

    public List<radioOptions> getListas() {
        return listas;
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
