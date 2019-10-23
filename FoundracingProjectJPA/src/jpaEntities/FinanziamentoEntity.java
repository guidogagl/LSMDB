package jpaEntities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "finanziamento", schema = "esercizio1", catalog = "")
public class FinanziamentoEntity {
    private int id;
    private Integer budget;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "nomeAzienda",
            nullable = false
    )
    private AziendaEntity stakeHolder;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id",
            nullable = false
    )
    private ProgettoEntity stakedProject;
    
    
    public FinanziamentoEntity(
    							Integer _budget,
    							AziendaEntity _azienda,
    							ProgettoEntity _progetto) {
    	this.budget = _budget;
    	this.stakeHolder = _azienda;
    	this.stakedProject = _progetto;
    }
    
    

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
