import java.io.Serializable;
import java.rmi.*;

public class DepMesa implements Serializable {
    String departamento;

    public DepMesa(String departamento){
        this.departamento = departamento;
    }
}
