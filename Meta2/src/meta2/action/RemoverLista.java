package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoverLista extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> listas;
    private String lista;

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
                counter+=1;
            }
        }
        if(lista==null) {
            addFieldError("tError", "Tipo da Lista é Obrigatorio.");
        }

    }

    @Override
    public String execute() throws RemoteException {
        this.getHeyBean().setLista(lista);
        this.getHeyBean().removeLista();
        return SUCCESS;
    }

    public List<radioOptions> getListas() {
        return listas;
    }

    public void setLista(String lista) {
        this.lista = lista;
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
