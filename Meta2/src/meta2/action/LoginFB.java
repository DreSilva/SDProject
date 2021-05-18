package meta2.action;

import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import models.User;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;


public class LoginFB extends ActionSupport implements SessionAware {
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final String apiKey = "1002478766825526";
    private static final String apiSecret = "c4fcf2bd00fa673e8c8c6c6e4cd35707";
    private static final Token EMPTY_TOKEN = null;
    private OAuthService service;
    private Map<String, Object> session;
    private String code;
    private User user = null;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public void validate() {
        try {
            this.service = this.getHeyBean().getService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Verifier verifier = new Verifier(this.code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        try {
            this.getHeyBean().setToken(accessToken);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();
        if (response.getCode() == 200) {
            String body = response.getBody();
            String id = body.split("[:}{,\"]")[11];
            try {
                this.getHeyBean().setFBid(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                this.user = this.getHeyBean().LoginFB();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (this.user == null) {
                addFieldError("tError", "A conta não está associada a nenhum user");
            }
        }

    }

    @Override
    public String execute() throws RemoteException {
        this.user = this.getHeyBean().LoginFB();
        this.getHeyBean().setUsername(this.user.getUser());
        this.getHeyBean().setPassword(this.user.getPassword());
        this.getHeyBean().setCC(this.user.getCC());
        this.getHeyBean().setUserLogIn(1);
        System.out.println("login com sucesso");
        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }
}
