import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    String user,password,departamento,faculdade,contacto,tipo,morada,CC;
    Date validade;
    Map<Eleicao,String> localVoto = new HashMap<>();

    public User(String user, String password, String departamento, String faculdade,
                        String contacto, String tipo, String morada, String CC, Date validade){
        this.CC=CC;
        this.user=user;
        this.password=password;
        this.departamento=departamento;
        this.faculdade=faculdade;
        this.contacto=contacto;
        this.tipo=tipo;
        this.morada=morada;
        this.validade=validade;
    }




}
