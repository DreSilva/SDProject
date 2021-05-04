package models;

import java.io.Serializable;

public class DepMesa implements Serializable {
    private static final long serialVersionUID = 4L;
    String departamento;
    long id;

    /**
     * Cria objeto para se saber em qual departamento esta a mesa
     * @param departamento Nome do departamento
     */
    public DepMesa(String departamento,int id){
        this.departamento = departamento;
        this.id = id;
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

    /**
     * Adiciona ID da mesa
     * @param id Id da mesa
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Compara ids entre mesas
     * @param o Objeto a comparar
     * @return True se for igual False se nao for
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepMesa depMesa = (DepMesa) o;

        return id == depMesa.id;
    }
}
