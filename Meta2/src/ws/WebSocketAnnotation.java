package ws;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation extends ActionSupport implements SessionAware {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();
    private Session session;
    private static Map<String, Object> session2;
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    @OnOpen
    public void start(Session session) throws RemoteException {
        this.session = session;
        users.add(this);
        ArrayList<String> s = this.getHeyBean().getUsersOnline();
        this.sendMessage("Users conectados:");
        for (String s1: s) {
            this.sendMessage("-"+s1);
        }

    }

    @OnClose
    public void end() {
    	users.remove(this);
    }

    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    public void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
        for (WebSocketAnnotation ws: users) {
            try {

                ws.session.getBasicRemote().sendText(text);
            } catch (IOException e) {
                // clean up once the WebSocket connection is closed
                try {
                    ws.session.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    public HeyBean getHeyBean() throws RemoteException {
        if(!session2.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session2.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session2.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session2 = session;
    }



}
