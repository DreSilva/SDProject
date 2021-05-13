/**
 * Raul Barbosa 2014-11-07
 */
package meta2.action;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import meta2.models.radioOptions;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String username = null, password = null, CC = null;
	private List<radioOptions> eleicoes;
	private String authorizationUrl;
	private OAuth20Service service;
	private static final String apiKey = "1002478766825526";
	private static final String apiSecret = "c4fcf2bd00fa673e8c8c6c6e4cd35707";
	private String secretState;

	@Override
	public String execute() throws RemoteException {
		this.service = new ServiceBuilder(apiKey)
				.apiSecret(apiSecret)
				.callback("https://localhost:8443/facebooklogin.action")
				.build(FacebookApi.instance());
		this.secretState = "secret"+ new Random().nextInt(999999);
		String authorizationUrl = this.service.getAuthorizationUrl(secretState);
		if(this.username != null && !username.equals("")) {
			this.getHeyBean().setUsername(this.username);
			this.getHeyBean().setPassword(this.password);
			this.getHeyBean().setCC(this.CC);
			if (this.getHeyBean().checkUserExists()) {
				eleicoes = new ArrayList<radioOptions>();
				int counter = 0;
				ArrayList<String> s = this.getHeyBean().getEleicao();
				for (String a: s) {
					eleicoes.add(new radioOptions(String.valueOf(counter),a));
					counter+=1;
				}
				return SUCCESS;
			} else {
				this.getHeyBean().setUsername("");
				this.getHeyBean().setPassword("");
				return LOGIN;

			}
		}
		else
			return LOGIN;
	}
	
	public void setUsername(String username) {
		this.username = username; // will you sanitize this input? maybe use a prepared statement?
	}

	public void setPassword(String password) {
		this.password = password; // what about this input? 
	}

	public String getAuthorizationURL(){
		return this.authorizationUrl;
	}

	public List<radioOptions> getEleicoes() {
		return eleicoes;
	}

	public void setCC(String CC) {
		this.CC = CC;
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
