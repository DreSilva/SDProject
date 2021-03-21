import java.rmi.Remote;

public interface Notifications extends Remote {
    public void estadoMesa() throws  java.rmi.RemoteException;
    public void fimEleicao(String nome,String votos) throws  java.rmi.RemoteException;
    public void novoEleitor() throws  java.rmi.RemoteException;
}
