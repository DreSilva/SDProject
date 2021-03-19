import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable {
    String user,password,departamento,contacto,tipo,morada,CC;
    Date validade;
    Map<Eleicao,String> localVoto = new HashMap<>();

    public User(String user, String password, String departamento,
                        String contacto, String tipo, String morada, String CC, Date validade){
        this.CC=CC;
        this.user=user;
        this.password=password;
        this.departamento=departamento;
        this.contacto=contacto;
        this.tipo=tipo;
        this.morada=morada;
        this.validade=validade;
    }


    public void addVoto(Eleicao eleicao, String departamento){
        localVoto.put(eleicao,departamento);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user1 = (User) o;
        return user.equals(user1.user) && password.equals(user1.password) && departamento.equals(user1.departamento) && contacto.equals(user1.contacto) && tipo.equals(user1.tipo) && morada.equals(user1.morada) && CC.equals(user1.CC) && validade.equals(user1.validade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, password, departamento, contacto, tipo, morada, CC, validade);
    }
}
