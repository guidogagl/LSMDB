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
    private Integer budget;
    private String descrizione;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "nomeAzienda",
            nullable = false
    )
    private AziendaEntity aziendaOwner;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "progetto"
    )
    private List<FinanziamentoEntity> stakes = null;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "progetto"
    )
    private List<ProgettoEntity> messaggi = null;
    
    public ProgettoEntity(
    					  	String nome,
    					  	Integer budget,
    					  	String descrizione,
    					    AziendaEntity aziendaOwner
    					  ) 
    {
    	this.nome = nome;
    	this.budget = budget;
    	this.descrizione = descrizione;
    	this.aziendaOwner=aziendaOwner;
    }
    
    public void addStakes( FinanziamentoEntity stake)
    {
    	 if(stakes == null )
             stakes =  new ArrayList<FinanziamentoEntity>();

         stakes.add( stake );
    }
    
    public void addMessaggio( ProgettoEntity messaggio)
    {
    	 if(messaggi == null )
             messaggi =  new ArrayList<ProgettoEntity>();

         messaggi.add( messaggio );
    }


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

    @Basic
    @Column(name = "azienda", nullable = true, length = 500)
    public String getOwner() {
        return aziendaOwner.getNomeAzienda();
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
