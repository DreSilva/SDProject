import java.io.Serializable;
import java.rmi.*;

public class DepMesa implements Serializable {
    String departamento;

    /**
     * Cria objeto para se saber em qual departamento esta a mesa
     * @param departamento Nome do departamento
     */
    public DepMesa(String departamento){
        this.departamento = departamento;
    }

    /**
     * Mesa sem departamento para mais tarde se atribuir um
     */
    public DepMesa(){}

    /**
     * Adicionar departamento a mesa
     * @param departamento departamento a adicionar
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
