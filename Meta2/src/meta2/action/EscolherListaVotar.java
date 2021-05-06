package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EscolherListaVotar extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String eleicao;
    private List<radioOptions> eleicoes,listas;

    @Override
    public void validate() {
        if(eleicoes==null){
            eleicoes = new ArrayList<radioOptions>();
            int counter = 0;
            ArrayList<String> s = null;
            try {
                s = this.getHeyBean().getEleicao();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s) {
                eleicoes.add(new radioOptions(String.valueOf(counter),a));
                counter+=1;
            }
        }
        if(eleicao==null){
            addFieldError("tError", "Eleição é Obrigatorio.");
        }
    }

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setEleicao(eleicao);
        ArrayList<String> s = this.getHeyBean().getListasEleicao();
        int counter = 0;
        listas = new ArrayList<>();
        for (String list:s) {
            listas.add(new radioOptions(String.valueOf(counter),list));
            counter+=1;
        }
        return SUCCESS;
    }

    public void setEleicao(String eleicao) {
        this.eleicao = eleicao;
    }

    public List<radioOptions> getListas() {
        return listas;
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
