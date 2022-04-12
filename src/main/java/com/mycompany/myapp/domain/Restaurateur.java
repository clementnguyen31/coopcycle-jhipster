package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Restaurateur.
 */
@Entity
@Table(name = "restaurateur")
public class Restaurateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurateur_id")
    private Long restaurateurId;

    @Column(name = "restaurateur_nom")
    private String restaurateurNom;

    @Column(name = "restaurateur_prenom")
    private String restaurateurPrenom;

    @Column(name = "restaurateur_boutique")
    private String restaurateurBoutique;

    @ManyToMany
    @JoinTable(
        name = "rel_restaurateur__client",
        joinColumns = @JoinColumn(name = "restaurateur_id"),
        inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    @JsonIgnoreProperties(value = { "recherches", "commande", "cooperatives", "restaurateurs" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "clients", "restaurateurs" }, allowSetters = true)
    private Commande commande;

    @ManyToMany(mappedBy = "restaurateurs")
    @JsonIgnoreProperties(value = { "livreurs", "restaurateurs", "clients" }, allowSetters = true)
    private Set<Cooperative> cooperatives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurateurId() {
        return this.restaurateurId;
    }

    public Restaurateur restaurateurId(Long restaurateurId) {
        this.setRestaurateurId(restaurateurId);
        return this;
    }

    public void setRestaurateurId(Long restaurateurId) {
        this.restaurateurId = restaurateurId;
    }

    public String getRestaurateurNom() {
        return this.restaurateurNom;
    }

    public Restaurateur restaurateurNom(String restaurateurNom) {
        this.setRestaurateurNom(restaurateurNom);
        return this;
    }

    public void setRestaurateurNom(String restaurateurNom) {
        this.restaurateurNom = restaurateurNom;
    }

    public String getRestaurateurPrenom() {
        return this.restaurateurPrenom;
    }

    public Restaurateur restaurateurPrenom(String restaurateurPrenom) {
        this.setRestaurateurPrenom(restaurateurPrenom);
        return this;
    }

    public void setRestaurateurPrenom(String restaurateurPrenom) {
        this.restaurateurPrenom = restaurateurPrenom;
    }

    public String getRestaurateurBoutique() {
        return this.restaurateurBoutique;
    }

    public Restaurateur restaurateurBoutique(String restaurateurBoutique) {
        this.setRestaurateurBoutique(restaurateurBoutique);
        return this;
    }

    public void setRestaurateurBoutique(String restaurateurBoutique) {
        this.restaurateurBoutique = restaurateurBoutique;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Restaurateur clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Restaurateur addClient(Client client) {
        this.clients.add(client);
        client.getRestaurateurs().add(this);
        return this;
    }

    public Restaurateur removeClient(Client client) {
        this.clients.remove(client);
        client.getRestaurateurs().remove(this);
        return this;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Restaurateur commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Set<Cooperative> getCooperatives() {
        return this.cooperatives;
    }

    public void setCooperatives(Set<Cooperative> cooperatives) {
        if (this.cooperatives != null) {
            this.cooperatives.forEach(i -> i.removeRestaurateur(this));
        }
        if (cooperatives != null) {
            cooperatives.forEach(i -> i.addRestaurateur(this));
        }
        this.cooperatives = cooperatives;
    }

    public Restaurateur cooperatives(Set<Cooperative> cooperatives) {
        this.setCooperatives(cooperatives);
        return this;
    }

    public Restaurateur addCooperative(Cooperative cooperative) {
        this.cooperatives.add(cooperative);
        cooperative.getRestaurateurs().add(this);
        return this;
    }

    public Restaurateur removeCooperative(Cooperative cooperative) {
        this.cooperatives.remove(cooperative);
        cooperative.getRestaurateurs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurateur)) {
            return false;
        }
        return id != null && id.equals(((Restaurateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurateur{" +
            "id=" + getId() +
            ", restaurateurId=" + getRestaurateurId() +
            ", restaurateurNom='" + getRestaurateurNom() + "'" +
            ", restaurateurPrenom='" + getRestaurateurPrenom() + "'" +
            ", restaurateurBoutique='" + getRestaurateurBoutique() + "'" +
            "}";
    }
}
