import java.io.Serializable;
import java.rmi.*;

public class DepMesa implements Serializable {
    String departamento;

    public DepMesa(String departamento){
        this.departamento = departamento;
    }
    public DepMesa(){}

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
