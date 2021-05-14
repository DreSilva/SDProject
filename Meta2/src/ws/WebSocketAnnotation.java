package ws;

import com.opensymphony.xwork2.ActionSupport;
import meta2.models.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/{room}")
public class WebSocketAnnotation extends ActionSupport implements SessionAware {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private Session session;
    private static Map<String, Object> session2;
    private static final Map<String,Session> sessions = Collections.synchronizedMap(new HashMap<>());
    public String room;
    private static String lastMessage = "";

    public WebSocketAnnotation(){}

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    @OnOpen
    public void start(final Session session, @PathParam("room") final String room) throws RemoteException, InterruptedException {
        this.session = session;
        session.getUserProperties().put("room",room);
        sessions.put(String.valueOf(session.getId()),session);
        ArrayList<String > s;
        if(room.equals("0")) {
            s = this.getHeyBean().getUsersOnline();
            this.sendMessage("Users conectados:", room);
            for (String s1: s) {
                this.sendMessage("-"+s1,room);
                Thread.sleep(50);
            }
        }
        else{
            s = this.getHeyBean().infoEleicao(Integer.parseInt(room));
            for (String s1: s) {
                this.sendMessage(s1,room);
                Thread.sleep(50);
            }
        }

    }

    @OnClose
    public void end() {
        sessions.remove(session.getId());
    }

    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    public void sendMessage(String text,String roomnumber) {
        synchronized (sessions){
            for(Map.Entry<String,Session> entry: sessions.entrySet()){
                Session s = entry.getValue();
                if(s.isOpen() && s.getUserProperties().get("room").equals(roomnumber)){
                    if(!text.equals(lastMessage)) {
                        entry.getValue().getAsyncRemote().sendText(text);
                        lastMessage=text;
                    }
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