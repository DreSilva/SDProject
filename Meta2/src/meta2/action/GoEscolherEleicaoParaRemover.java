package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoEscolherEleicaoParaRemover extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> eleicoes;

    @Override
    public String execute() throws RemoteException {
        eleicoes = new ArrayList<radioOptions>();
        int counter = 0;
        ArrayList<String> s = this.getHeyBean().getEleicao();
        for (String a: s) {
            eleicoes.add(new radioOptions(String.valueOf(counter),a));
            counter+=1;
        }
        return SUCCESS;
    }


    public List<radioOptions> getEleicoes() {
        return eleicoes;
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
