package jpaEntities;

import javax.persistence.*;
import javax.xml.ws.http.HTTPBinding;
import java.util.Objects;

@Entity
@Table(name = "finanziamento", schema = "esercizio1", catalog = "")
public class FinanziamentoEntity {
    private int id;
    private Integer budget;

    private AziendaEntity azienda;
    private ProgettoEntity progetto;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "azienda_id", nullable = false)
    public AziendaEntity getAzienda(){ return this.azienda ;};
    public void setAzienda(AziendaEntity _azienda){ this.azienda = _azienda; };

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progetto_id", nullable = false)
    public ProgettoEntity getProgetto(){ return this.progetto ;};
    public void setProgetto(ProgettoEntity _progetto){ this.progetto = _progetto; };

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

}
