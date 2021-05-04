package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoverUserLista extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private List<radioOptions> users;
    private ArrayList<String> user;

    @Override
    public void validate() {
        if(users==null){
            this.users = new ArrayList<>();
            int counter = 0;
            try {
                for (String s: this.getHeyBean().getUsersList()) {
                    this.users.add(new radioOptions(String.valueOf(counter),s));
                    counter+=1;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String execute() throws RemoteException {
        for (String user: this.user) {
            this.getHeyBean().removerUserLista(Integer.parseInt(user));
        }
        return SUCCESS;
    }


    public List<radioOptions> getUsers() {
        return users;
    }

    public void setUser(ArrayList<String> user) {
        this.user = user;
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
