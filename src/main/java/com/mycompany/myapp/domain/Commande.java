package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "commande_id")
    private Long commandeId;

    @Column(name = "commande_lieu")
    private String commandeLieu;

    @Column(name = "commande_echeance")
    private String commandeEcheance;

    @Column(name = "commande_libelle")
    private String commandeLibelle;

    @OneToMany(mappedBy = "commande")
    @JsonIgnoreProperties(value = { "recherches", "commande", "cooperatives", "restaurateurs" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @OneToMany(mappedBy = "commande")
    @JsonIgnoreProperties(value = { "clients", "commande", "cooperatives" }, allowSetters = true)
    private Set<Restaurateur> restaurateurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommandeId() {
        return this.commandeId;
    }

    public Commande commandeId(Long commandeId) {
        this.setCommandeId(commandeId);
        return this;
    }

    public void setCommandeId(Long commandeId) {
        this.commandeId = commandeId;
    }

    public String getCommandeLieu() {
        return this.commandeLieu;
    }

    public Commande commandeLieu(String commandeLieu) {
        this.setCommandeLieu(commandeLieu);
        return this;
    }

    public void setCommandeLieu(String commandeLieu) {
        this.commandeLieu = commandeLieu;
    }

    public String getCommandeEcheance() {
        return this.commandeEcheance;
    }

    public Commande commandeEcheance(String commandeEcheance) {
        this.setCommandeEcheance(commandeEcheance);
        return this;
    }

    public void setCommandeEcheance(String commandeEcheance) {
        this.commandeEcheance = commandeEcheance;
    }

    public String getCommandeLibelle() {
        return this.commandeLibelle;
    }

    public Commande commandeLibelle(String commandeLibelle) {
        this.setCommandeLibelle(commandeLibelle);
        return this;
    }

    public void setCommandeLibelle(String commandeLibelle) {
        this.commandeLibelle = commandeLibelle;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setCommande(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setCommande(this));
        }
        this.clients = clients;
    }

    public Commande clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Commande addClient(Client client) {
        this.clients.add(client);
        client.setCommande(this);
        return this;
    }

    public Commande removeClient(Client client) {
        this.clients.remove(client);
        client.setCommande(null);
        return this;
    }

    public Set<Restaurateur> getRestaurateurs() {
        return this.restaurateurs;
    }

    public void setRestaurateurs(Set<Restaurateur> restaurateurs) {
        if (this.restaurateurs != null) {
            this.restaurateurs.forEach(i -> i.setCommande(null));
        }
        if (restaurateurs != null) {
            restaurateurs.forEach(i -> i.setCommande(this));
        }
        this.restaurateurs = restaurateurs;
    }

    public Commande restaurateurs(Set<Restaurateur> restaurateurs) {
        this.setRestaurateurs(restaurateurs);
        return this;
    }

    public Commande addRestaurateur(Restaurateur restaurateur) {
        this.restaurateurs.add(restaurateur);
        restaurateur.setCommande(this);
        return this;
    }

    public Commande removeRestaurateur(Restaurateur restaurateur) {
        this.restaurateurs.remove(restaurateur);
        restaurateur.setCommande(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", commandeId=" + getCommandeId() +
            ", commandeLieu='" + getCommandeLieu() + "'" +
            ", commandeEcheance='" + getCommandeEcheance() + "'" +
            ", commandeLibelle='" + getCommandeLibelle() + "'" +
            "}";
    }
}
