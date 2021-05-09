package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoAdicionarUserLista extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> listas, users;

    @Override
    public String execute() throws RemoteException {
        listas = new ArrayList<radioOptions>();
        users = new ArrayList<radioOptions>();
        int counter = 0;
        ArrayList<String> s = this.getHeyBean().getListas();
        for (String a: s) {
            listas.add(new radioOptions(String.valueOf(counter),a));
            counter+=1;
        }
        counter = 0;
        ArrayList<String> s2 = this.getHeyBean().getUsers();
        for (String a: s2) {
            users.add(new radioOptions(String.valueOf(counter),a));
            counter+=1;
        }
        return SUCCESS;
    }


    public List<radioOptions> getListas() {
        return listas;
    }

    public List<radioOptions> getUsers() {
        return users;
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
