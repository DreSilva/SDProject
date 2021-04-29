package meta2.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class AdminLoginAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String password;

    @Override
    public String execute() {
        if(password.equals("1234")){
            return SUCCESS;
        }
        else{
            return LOGIN;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
