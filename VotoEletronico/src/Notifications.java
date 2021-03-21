import java.rmi.Remote;

public interface Notifications extends Remote {
    /**
     * Função que notifica a admin console sobre o estado da mesa
     */
    public void estadoMesa() throws  java.rmi.RemoteException;

    /**
     * Função que notifica a admin console quando uma eleição termina
     * @param nome Nome da eleição
     * @param votos Votos que cada lista obteve
     */
    public void fimEleicao(String nome,String votos) throws  java.rmi.RemoteException;

    /**
     * Função que notifica a admin console quando existe um novo eleitor numa mesa
     */
    public void novoEleitor() throws  java.rmi.RemoteException;
}
