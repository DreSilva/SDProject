package rmiserver;

import java.rmi.Remote;

public interface Notifications extends Remote {
    /**
     * Função que notifica a admin console sobre o estado da mesa
     * @param estado estado da maquina, on ou off
     * @param dep departamento da maquina
     * @param num id da maquina
     */
    public void estadoMesa(String estado,String dep,long num) throws  java.rmi.RemoteException;

    /**
     * Função que notifica a admin console quando uma eleição termina
     * @param nome Nome da eleição
     * @param votos Votos que cada lista obteve
     */
    public void fimEleicao(String nome,String votos) throws  java.rmi.RemoteException;

}
