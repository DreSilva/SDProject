package meta2.action;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;


import java.util.Map;
import java.util.Random;


public class AssociarFB extends ActionSupport implements SessionAware {
    private static final String PROTECTED_RESOURSE_URL="https://graph.faceboperaok.com/me";
    private static final String apiKey = "1002478766825526";
    private static final String apiSecret = "c4fcf2bd00fa673e8c8c6c6e4cd35707";
    private OAuth20Service service;
    private String secretState;
    private Map<String, Object> session;


    @Override
    public String execute(){
        this.service = new ServiceBuilder(apiKey)
                .apiSecret(apiSecret)
                .callback("https://localhost:8443/facebooklogin.action")
                .build(FacebookApi.instance());
        this.secretState = "secret"+ new Random().nextInt(999999);

        return null;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session=session;
    }
}
