package jpaEntities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "azienda", schema = "esercizio1", catalog = "")
public class AziendaEntity {
    private String nomeAzienda;
    private String urlLogo;
    private String urlSito;
    private String indirizzo;
    private Integer cap;
    private String password;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "stakeHolder"
    )
    private List<FinanziamentoEntity> finanziamento = null;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "aziendaOwner"
    )
    private List<ProgettoEntity> myProjects = null;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "aziendaDestinataria"
    )
    private List<MessaggioEntity> ricevuti = null;

    @Id
    @Column(name = "nomeAzienda", nullable = false, length = 500)
    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

    @Basic
    @Column(name = "urlLogo", nullable = true, length = 500)
    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    @Basic
    @Column(name = "urlSito", nullable = true, length = 500)
    public String getUrlSito() {
        return urlSito;
    }

    public void setUrlSito(String urlSito) {
        this.urlSito = urlSito;
    }

    @Basic
    @Column(name = "indirizzo", nullable = true, length = 500)
    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    @Basic
    @Column(name = "cap", nullable = true)
    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AziendaEntity that = (AziendaEntity) o;
        return Objects.equals(nomeAzienda, that.nomeAzienda) &&
                Objects.equals(urlLogo, that.urlLogo) &&
                Objects.equals(urlSito, that.urlSito) &&
                Objects.equals(indirizzo, that.indirizzo) &&
                Objects.equals(cap, that.cap) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeAzienda, urlLogo, urlSito, indirizzo, cap, password);
    }
}
