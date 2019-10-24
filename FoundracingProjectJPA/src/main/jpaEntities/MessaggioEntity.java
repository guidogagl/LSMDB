package jpaEntities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "messaggio", schema = "esercizio1", catalog = "")
public class MessaggioEntity {
    private int id;
    private String testo;
    private int stake;
    private Date data;

    private AziendaEntity destinatario;
    private AziendaEntity mittente;
    private ProgettoEntity progetto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    public AziendaEntity getDestinatario(){ return this.destinatario ;};
    public void setDestinatario(AziendaEntity _destinatario){ this.destinatario = _destinatario; };

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mittente_id", nullable = false)
    public AziendaEntity getMittente(){ return this.mittente ;};
    public void setMittente(AziendaEntity _mittente){ this.mittente = _mittente; };

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
