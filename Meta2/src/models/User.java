package models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class que corresponde a cada utilizador assim como os seus atributos necessários
 */
public class User implements Serializable {
    private static final long serialVersionUID = 4L;
    String user,password,departamento,contacto,tipo,morada,CC;
    Date validade;
    Map<Eleicao,String> localVoto = new HashMap<>();


    /**
     * Objeto user tem as caracteristicas necessárias
     * @param user Nome do utilizador
     * @param password Password do utilizador
     * @param departamento Departamento do utilizador
     * @param contacto Contato do utilizador
     * @param tipo Tipo do utilizador
     * @param morada Morada do utilizador
     * @param CC Cartao de Cidadao do utilizador
     * @param validade Validade do CC
     */
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

    /**
     * Guarda local de voto e departamento onde votou
     * @param eleicao Eleição ao qual votou
     * @param departamento Departamento onde votou
     */
    public void addVoto(Eleicao eleicao, String departamento){
        localVoto.put(eleicao,departamento);
    }


    /**
     * Comparação entre users
     * @param o Utilizador a comprar
     * @return True se  for igual false se nao for igual
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user1 = (User) o;
        return user.equals(user1.user) && password.equals(user1.password)  && CC.equals(user1.CC);
    }

    public String getCC() {
        return CC;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }
}
