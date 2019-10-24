package jpaEntities;

import javax.persistence.*;
import java.util.ArrayList;
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

    private List<FinanziamentoEntity> myStakes;
    private List<ProgettoEntity> myProjects;
    private List<MessaggioEntity> msgsReceived;
    private List<MessaggioEntity> msgsSended;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "azienda")
    public List<FinanziamentoEntity> getMyStakes(){ return this.myStakes ;};
    public void setMyStakes(List<FinanziamentoEntity> _stakes){ this.myStakes = _stakes; };

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "azienda")
    public List<ProgettoEntity> getMyProjects(){ return this.myProjects ;};
    public void setMyProjects(List<ProgettoEntity> _project){ this.myProjects = _project; };

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "destinatario")
    public List<MessaggioEntity> getMsgsReceived(){ return this.msgsReceived ;};
    public void setMsgsReceived(List<MessaggioEntity> _msgs){ this.msgsReceived = _msgs; };

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "mittente")
    public List<MessaggioEntity> getMsgsSended(){ return this.msgsSended ;};
    public void setMsgsSended(List<MessaggioEntity> _msgs){ this.msgsSended = _msgs; };


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

}
