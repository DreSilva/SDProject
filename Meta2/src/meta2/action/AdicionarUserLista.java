package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdicionarUserLista extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> listas, users;
    private String lista;
    private ArrayList<String> user;

    @Override
    public void validate() {
        if(listas==null){
            listas = new ArrayList<radioOptions>();
            int counter = 0;
            ArrayList<String> s = null;
            try {
                s = this.getHeyBean().getListas();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s) {
                listas.add(new radioOptions(String.valueOf(counter),a));
            }
        }
        if(users==null){
            users = new ArrayList<radioOptions>();
            int counter = 0;
            ArrayList<String> s2 = null;
            try {
                s2 = this.getHeyBean().getUsers();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            for (String a: s2) {
                users.add(new radioOptions(String.valueOf(counter),a));
                counter+=1;
            }
        }
        if(lista==null){
            addFieldError("tError", "Lista é Obrigatoria.");
        }
        if(user==null){
            addFieldError("tError", "Utilizadores são Obrigatorios.");
        }

    }

    @Override
    public String execute() throws RemoteException {
        this.getHeyBean().setLista(lista);
        for (String s: user) {
            this.getHeyBean().addUserLista(Integer.parseInt(s));
        }

        return SUCCESS;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public void setUser(ArrayList<String> user) {
        this.user = user;
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
