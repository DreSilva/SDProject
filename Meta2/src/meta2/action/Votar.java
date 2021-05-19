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

public class Votar extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private static final Token EMPTY_TOKEN = null;
    private Map<String, Object> session;
    private String lista;
    private List<radioOptions> listas;
    private OAuthService service;
    private String authorizationUrl;

    @Override
    public void validate() {
        if(listas==null){
            ArrayList<String> s = null;
            try {
                s = this.getHeyBean().getListasEleicao();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            int counter = 0;
            listas = new ArrayList<>();
            for (String list:s) {
                listas.add(new radioOptions(String.valueOf(counter),list));
                counter+=1;
            }
        }
        if(lista==null){
            addFieldError("tError", "Lista é Obrigatorio.");
        }
        try {
            int n = this.getHeyBean().checkVotar();
            if(n==0){
                addFieldError("tError", "A votacao nesta eleicao ainda nao comecou");
            }
            if(n==1){
                addFieldError("tError", "Esta mesa nao se encontra registada para esta eleicao");
            }
            if(n==2){
                addFieldError("tError", "Utilizador ja votou nesta eleicao");
            }
            if(n==3){
                addFieldError("tError", "User nao pode votar nesta eleicao, e para uma funcao diferente!");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String execute() throws Exception {
        this.getHeyBean().setLista(lista);
        this.getHeyBean().votar();

        this.service = new ServiceBuilder()
                .apiKey(this.getHeyBean().getApiKey())
                .provider(FacebookApi2.class)
                .apiSecret(this.getHeyBean().getApiSecret())
                .callback("http://localhost:8080/meta2/index")
                .scope("public_profile")
                .build();
        int nEleicao = this.getHeyBean().getIntEleicao()+1;
        this.authorizationUrl = this.service.getShareUrl(EMPTY_TOKEN,"http://votacao.com:8080/meta2/notifications/"+nEleicao);
        this.getHeyBean().setAuthorizationUrl(authorizationUrl);
        return SUCCESS;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public List<radioOptions> getListas() {
        return listas;
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
