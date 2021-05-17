/**
 * Raul Barbosa 2014-11-07
 */
package meta2.action;

import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class getUrlFB extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null, password = null, CC = null;
    private List<radioOptions> eleicoes;
    private String authorizationUrl;
    private OAuthService service;
    private static final Token EMPTY_TOKEN = null;
    @Override
    public String execute() throws RemoteException {
        this.service = this.getHeyBean().getService("http://localhost:8080/meta2/loginfb");
        this.authorizationUrl = this.service.getAuthorizationUrl(EMPTY_TOKEN);
        this.getHeyBean().setAuthorizationUrl(authorizationUrl);
        return SUCCESS;
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

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }
}
