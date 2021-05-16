package meta2.action;

import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;


public class AssociarFB extends ActionSupport implements SessionAware {
    private static final String PROTECTED_RESOURSE_URL="https://graph.faceboperaok.com/me";
    private static final String apiKey = "1002478766825526";
    private static final String apiSecret = "c4fcf2bd00fa673e8c8c6c6e4cd35707";
    private OAuthService service;
    private Map<String, Object> session;
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String execute() throws RemoteException {
        //this.code= this.getHeyBean().getCode();
        System.out.println("helllllllllllllllllooooooooooooooooooo");
        this.service = this.getHeyBean().getService("http://localhost:8080/meta2/associarfb.action");

        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session=session;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if(!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
