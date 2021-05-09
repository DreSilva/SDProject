package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EscolherEleicaoGerir extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private List<radioOptions> eleicoes;
    private String eleicao;

    @Override
    public void validate() {
        if(eleicoes==null){
            eleicoes = new ArrayList<radioOptions>();
            int counter = 0;
            try {
                ArrayList<String> s = this.getHeyBean().getEleicao();
                for (String a: s) {
                    eleicoes.add(new radioOptions(String.valueOf(counter),a));
                    counter+=1;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if(eleicao==null){
            addFieldError("tError", "Eleição é obrigatorio.");
        }
    }

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setEleicao(eleicao);
        return SUCCESS;
    }

    public List<radioOptions> getEleicoes() {
        return eleicoes;
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
