import java.rmi.*;

public interface RMIClient  extends Remote{
    public void criarMesa(String departamento) throws java.rmi.RemoteException;
}
