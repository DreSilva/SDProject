package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;
import meta2.model.HeyBean;

public class SayHeyAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String userToHey = null;

    @Override
    public String execute() throws RemoteException {
        System.out.println("HERE");
        System.out.println(userToHey);
        this.getHeyBean().addMessageUser(this.userToHey);
        return SUCCESS;
    }

    public void setUserToHey(String userToHey) {
        this.userToHey = userToHey;
    }

    public HeyBean getHeyBean() {
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