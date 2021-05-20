package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoveMesasEleicao extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> mesas;
    private ArrayList<String> mesa;

    @Override
    public void validate() {
        if(mesa==null){
            ArrayList<String> s = null;
            try {
                s = this.getHeyBean().getMesasEleicao();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int count=0;
            for (String mesa: s) {
                mesas.add(new radioOptions(String.valueOf(count),mesa));
            }
        }
        if(mesa==null){
            addFieldError("tError", "Mesas a Adicionar são obrigatorias.");
        }
    }

    @Override
    public String execute() throws Exception {
        for (String mesa: this.mesa) {
            this.getHeyBean().removeMesaEleicao(0);
        }
        return SUCCESS;
    }

    public void setMesa(ArrayList<String> mesa) {
        this.mesa = mesa;
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
