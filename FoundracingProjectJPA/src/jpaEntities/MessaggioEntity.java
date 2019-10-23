package jpaEntities;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "messaggio", schema = "esercizio1", catalog = "")
public class MessaggioEntity {
    private int id;
    private String testo;
    private int stake;
    private Date data;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "nomeAzienda",
            nullable = false
    )
    private AziendaEntity aziendaDestinataria;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "id",
            nullable = false
    )
    private ProgettoEntity progetto;
    
    
    public MessaggioEntity(
    						String _testo,
    						int _stake,
    						Date _data,
    						AziendaEntity _azienda,
    						ProgettoEntity _progetto) {
    	this.testo = _testo;
    	this.stake = _stake;
    	this.data = _data;
    	this.aziendaDestinataria = _azienda;
    	this.progetto = _progetto;
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
    @Column(name = "testo", nullable = false, length = 500)
    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    @Basic
    @Column(name = "stake", nullable = false)
    public int getStake() {
        return stake;
    }

    public void setStake(int stake) {
        this.stake = stake;
    }

    @Basic
    @Column(name = "progetto", nullable = false)
    public int getProject() {
        return progetto.getId();
    }

    @Basic
    @Column(name = "destinatario", nullable = false)
    public String getDestinatario() {
        return aziendaDestinataria.getNomeAzienda();
    }

    @Basic
    @Column(name = "data", nullable = false)
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessaggioEntity that = (MessaggioEntity) o;
        return id == that.id &&
                stake == that.stake &&
                Objects.equals(testo, that.testo) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testo, stake, data);
    }
}
