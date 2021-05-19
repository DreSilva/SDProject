/**
 * Raul Barbosa 2014-11-07
 */
package meta2.action;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.oauth.OAuthService;
import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;
import uc.sd.apis.FacebookApi2;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadFinishedElections extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private static final Token EMPTY_TOKEN = null;
    private Map<String, Object> session;
    private String username = null, password = null, CC = null;
    private List<radioOptions> eleicoes;
    private OAuthService service;
    private String authorizationUrl;

    @Override
    public String execute() throws RemoteException {
        eleicoes = new ArrayList<radioOptions>();
        int counter = 0;
        ArrayList<String> s = this.getHeyBean().getEleicoesVelhas();
        for (String a : s) {
            eleicoes.add(new radioOptions(String.valueOf(counter), a));
            counter += 1;
        }

        return SUCCESS;
    }

    public List<radioOptions> getEleicoes() {
        return eleicoes;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if (!session.containsKey("heyBean"))
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
