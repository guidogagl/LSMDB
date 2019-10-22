package jpaEntities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "finanziamento", schema = "esercizio1", catalog = "")
public class FinanziamentoEntity {
    private int id;
    private Integer budget;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "budget", nullable = true)
    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinanziamentoEntity that = (FinanziamentoEntity) o;
        return id == that.id &&
                Objects.equals(budget, that.budget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, budget);
    }
}
