package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Recherche.
 */
@Entity
@Table(name = "recherche")
public class Recherche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "recherche_id")
    private Long rechercheId;

    @Column(name = "recherche_thematique")
    private String rechercheThematique;

    @Column(name = "recherche_geographique")
    private String rechercheGeographique;

    @ManyToMany(mappedBy = "recherches")
    @JsonIgnoreProperties(value = { "recherches", "commande", "cooperatives", "restaurateurs" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recherche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRechercheId() {
        return this.rechercheId;
    }

    public Recherche rechercheId(Long rechercheId) {
        this.setRechercheId(rechercheId);
        return this;
    }

    public void setRechercheId(Long rechercheId) {
        this.rechercheId = rechercheId;
    }

    public String getRechercheThematique() {
        return this.rechercheThematique;
    }

    public Recherche rechercheThematique(String rechercheThematique) {
        this.setRechercheThematique(rechercheThematique);
        return this;
    }

    public void setRechercheThematique(String rechercheThematique) {
        this.rechercheThematique = rechercheThematique;
    }

    public String getRechercheGeographique() {
        return this.rechercheGeographique;
    }

    public Recherche rechercheGeographique(String rechercheGeographique) {
        this.setRechercheGeographique(rechercheGeographique);
        return this;
    }

    public void setRechercheGeographique(String rechercheGeographique) {
        this.rechercheGeographique = rechercheGeographique;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.removeRecherche(this));
        }
        if (clients != null) {
            clients.forEach(i -> i.addRecherche(this));
        }
        this.clients = clients;
    }

    public Recherche clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Recherche addClient(Client client) {
        this.clients.add(client);
        client.getRecherches().add(this);
        return this;
    }

    public Recherche removeClient(Client client) {
        this.clients.remove(client);
        client.getRecherches().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recherche)) {
            return false;
        }
        return id != null && id.equals(((Recherche) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recherche{" +
            "id=" + getId() +
            ", rechercheId=" + getRechercheId() +
            ", rechercheThematique='" + getRechercheThematique() + "'" +
            ", rechercheGeographique='" + getRechercheGeographique() + "'" +
            "}";
    }
}
