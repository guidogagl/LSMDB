package jpaEntities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "progetto", schema = "esercizio1", catalog = "")
public class ProgettoEntity {
    private int id;
    private String nome;
    private Integer budget;
    private String descrizione;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nome", nullable = true, length = 500)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic
    @Column(name = "budget", nullable = true)
    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    @Basic
    @Column(name = "descrizione", nullable = true, length = 500)
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgettoEntity that = (ProgettoEntity) o;
        return id == that.id &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(budget, that.budget) &&
                Objects.equals(descrizione, that.descrizione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, budget, descrizione);
    }
}
