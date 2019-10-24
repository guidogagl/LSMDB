package jpaEntities;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "progetto", schema = "esercizio1", catalog = "")
public class ProgettoEntity {
    private int id;
    private String nome;
    private String descrizione;
    private Integer budget;

    private List<FinanziamentoEntity> myStakes;
    private List<MessaggioEntity> myMsgs;
    private AziendaEntity azienda;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "progetto")
    public List<FinanziamentoEntity> getMyStakes(){ return this.myStakes ;};
    public void setMyStakes(List<FinanziamentoEntity> _stakes){ this.myStakes = _stakes; };

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "progetto")
    public List<MessaggioEntity> getMyMsgs(){ return this.myMsgs ;};
    public void setMyMsgs(List<MessaggioEntity> _msgs){ this.myMsgs = _msgs; };

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    public AziendaEntity getAzienda(){ return this.azienda ;};
    public void setAzienda(AziendaEntity _azienda){ this.azienda = _azienda; };


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
