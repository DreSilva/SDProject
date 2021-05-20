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

public class loadEsta extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private static final Token EMPTY_TOKEN = null;
    private Map<String, Object> session;
    private String username = null, password = null, CC = null,eleicao=null;
    private List<radioOptions> eleicoes;
    private OAuthService service;
    private String authorizationUrl;

    @Override
    public String execute() throws RemoteException {
        this.service = new ServiceBuilder()
                .apiKey(this.getHeyBean().getApiKey())
                .provider(FacebookApi2.class)
                .apiSecret(this.getHeyBean().getApiSecret())
                .callback("http://localhost:8080/meta2/login")
                .scope("public_profile")
                .build();
        int n_election=Integer.parseInt(eleicao);
        ArrayList<String> arrOfStr = this.getHeyBean().infoEleicao(n_election);
        String statistics= "";
        for(String a : arrOfStr){
            statistics=statistics+a+'\n';
        }
        this.authorizationUrl = this.service.getShareUrl(EMPTY_TOKEN,"http://votacao.com:8080/meta2/escolherEleicaoPassada/"+eleicao, statistics);
        this.getHeyBean().setAuthorizationUrl(authorizationUrl);
        return SUCCESS;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public List<radioOptions> getEleicoes() {
        return eleicoes;
    }

    public HeyBean getHeyBean() throws RemoteException {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setEleicao(String eleicao) {
        this.eleicao = String.valueOf(Integer.parseInt(eleicao)+1);
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
