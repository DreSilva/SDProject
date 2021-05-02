package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EscolherListaParaRemover extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> listas;
    private String lista;

    @Override
    public void validate() {
        if(listas==null){
            listas = new ArrayList<radioOptions>();
            int counter = 0;
            ArrayList<String> s2 = null;
            try {
                s2 = this.getHeyBean().getListasEleicao();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s2) {
                listas.add(new radioOptions(String.valueOf(counter),a));
            }
        }
    }

    @Override
    public String execute() throws RemoteException {
        this.getHeyBean().setLista(lista);
        //TODO REMOVER LISTA
        return SUCCESS;
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
