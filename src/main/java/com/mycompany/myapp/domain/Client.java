package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "client_nom")
    private String clientNom;

    @Column(name = "client_prenom")
    private String clientPrenom;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "client_adresse")
    private String clientAdresse;

    @ManyToMany
    @JoinTable(
        name = "rel_client__recherche",
        joinColumns = @JoinColumn(name = "client_id"),
        inverseJoinColumns = @JoinColumn(name = "recherche_id")
    )
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Set<Recherche> recherches = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "clients", "restaurateurs" }, allowSetters = true)
    private Commande commande;

    @ManyToMany(mappedBy = "clients")
    @JsonIgnoreProperties(value = { "livreurs", "restaurateurs", "clients" }, allowSetters = true)
    private Set<Cooperative> cooperatives = new HashSet<>();

    @ManyToMany(mappedBy = "clients")
    @JsonIgnoreProperties(value = { "clients", "commande", "cooperatives" }, allowSetters = true)
    private Set<Restaurateur> restaurateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public Client clientId(Long clientId) {
        this.setClientId(clientId);
        return this;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientNom() {
        return this.clientNom;
    }

    public Client clientNom(String clientNom) {
        this.setClientNom(clientNom);
        return this;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getClientPrenom() {
        return this.clientPrenom;
    }

    public Client clientPrenom(String clientPrenom) {
        this.setClientPrenom(clientPrenom);
        return this;
    }

    public void setClientPrenom(String clientPrenom) {
        this.clientPrenom = clientPrenom;
    }

    public String getClientEmail() {
        return this.clientEmail;
    }

    public Client clientEmail(String clientEmail) {
        this.setClientEmail(clientEmail);
        return this;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientAdresse() {
        return this.clientAdresse;
    }

    public Client clientAdresse(String clientAdresse) {
        this.setClientAdresse(clientAdresse);
        return this;
    }

    public void setClientAdresse(String clientAdresse) {
        this.clientAdresse = clientAdresse;
    }

    public Set<Recherche> getRecherches() {
        return this.recherches;
    }

    public void setRecherches(Set<Recherche> recherches) {
        this.recherches = recherches;
    }

    public Client recherches(Set<Recherche> recherches) {
        this.setRecherches(recherches);
        return this;
    }

    public Client addRecherche(Recherche recherche) {
        this.recherches.add(recherche);
        recherche.getClients().add(this);
        return this;
    }

    public Client removeRecherche(Recherche recherche) {
        this.recherches.remove(recherche);
        recherche.getClients().remove(this);
        return this;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Client commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Set<Cooperative> getCooperatives() {
        return this.cooperatives;
    }

    public void setCooperatives(Set<Cooperative> cooperatives) {
        if (this.cooperatives != null) {
            this.cooperatives.forEach(i -> i.removeClient(this));
        }
        if (cooperatives != null) {
            cooperatives.forEach(i -> i.addClient(this));
        }
        this.cooperatives = cooperatives;
    }

    public Client cooperatives(Set<Cooperative> cooperatives) {
        this.setCooperatives(cooperatives);
        return this;
    }

    public Client addCooperative(Cooperative cooperative) {
        this.cooperatives.add(cooperative);
        cooperative.getClients().add(this);
        return this;
    }

    public Client removeCooperative(Cooperative cooperative) {
        this.cooperatives.remove(cooperative);
        cooperative.getClients().remove(this);
        return this;
    }

    public Set<Restaurateur> getRestaurateurs() {
        return this.restaurateurs;
    }

    public void setRestaurateurs(Set<Restaurateur> restaurateurs) {
        if (this.restaurateurs != null) {
            this.restaurateurs.forEach(i -> i.removeClient(this));
        }
        if (restaurateurs != null) {
            restaurateurs.forEach(i -> i.addClient(this));
        }
        this.restaurateurs = restaurateurs;
    }

    public Client restaurateurs(Set<Restaurateur> restaurateurs) {
        this.setRestaurateurs(restaurateurs);
        return this;
    }

    public Client addRestaurateur(Restaurateur restaurateur) {
        this.restaurateurs.add(restaurateur);
        restaurateur.getClients().add(this);
        return this;
    }

    public Client removeRestaurateur(Restaurateur restaurateur) {
        this.restaurateurs.remove(restaurateur);
        restaurateur.getClients().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", clientId=" + getClientId() +
            ", clientNom='" + getClientNom() + "'" +
            ", clientPrenom='" + getClientPrenom() + "'" +
            ", clientEmail='" + getClientEmail() + "'" +
            ", clientAdresse='" + getClientAdresse() + "'" +
            "}";
    }
}
