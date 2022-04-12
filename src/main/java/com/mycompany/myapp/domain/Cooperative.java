package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Cooperative.
 */
@Entity
@Table(name = "cooperative")
public class Cooperative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cooperative_id")
    private Long cooperativeId;

    @Column(name = "cooperative_nom")
    private String cooperativeNom;

    @Column(name = "cooperative_directeur")
    private String cooperativeDirecteur;

    @Column(name = "cooperative_ville")
    private String cooperativeVille;

    @Column(name = "cooperative_metropole")
    private String cooperativeMetropole;

    @Column(name = "cooperative_commune")
    private String cooperativeCommune;

    @ManyToMany
    @JoinTable(
        name = "rel_cooperative__livreur",
        joinColumns = @JoinColumn(name = "cooperative_id"),
        inverseJoinColumns = @JoinColumn(name = "livreur_id")
    )
    @JsonIgnoreProperties(value = { "cooperatives" }, allowSetters = true)
    private Set<Livreur> livreurs = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_cooperative__restaurateur",
        joinColumns = @JoinColumn(name = "cooperative_id"),
        inverseJoinColumns = @JoinColumn(name = "restaurateur_id")
    )
    @JsonIgnoreProperties(value = { "clients", "commande", "cooperatives" }, allowSetters = true)
    private Set<Restaurateur> restaurateurs = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_cooperative__client",
        joinColumns = @JoinColumn(name = "cooperative_id"),
        inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    @JsonIgnoreProperties(value = { "recherches", "commande", "cooperatives", "restaurateurs" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cooperative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCooperativeId() {
        return this.cooperativeId;
    }

    public Cooperative cooperativeId(Long cooperativeId) {
        this.setCooperativeId(cooperativeId);
        return this;
    }

    public void setCooperativeId(Long cooperativeId) {
        this.cooperativeId = cooperativeId;
    }

    public String getCooperativeNom() {
        return this.cooperativeNom;
    }

    public Cooperative cooperativeNom(String cooperativeNom) {
        this.setCooperativeNom(cooperativeNom);
        return this;
    }

    public void setCooperativeNom(String cooperativeNom) {
        this.cooperativeNom = cooperativeNom;
    }

    public String getCooperativeDirecteur() {
        return this.cooperativeDirecteur;
    }

    public Cooperative cooperativeDirecteur(String cooperativeDirecteur) {
        this.setCooperativeDirecteur(cooperativeDirecteur);
        return this;
    }

    public void setCooperativeDirecteur(String cooperativeDirecteur) {
        this.cooperativeDirecteur = cooperativeDirecteur;
    }

    public String getCooperativeVille() {
        return this.cooperativeVille;
    }

    public Cooperative cooperativeVille(String cooperativeVille) {
        this.setCooperativeVille(cooperativeVille);
        return this;
    }

    public void setCooperativeVille(String cooperativeVille) {
        this.cooperativeVille = cooperativeVille;
    }

    public String getCooperativeMetropole() {
        return this.cooperativeMetropole;
    }

    public Cooperative cooperativeMetropole(String cooperativeMetropole) {
        this.setCooperativeMetropole(cooperativeMetropole);
        return this;
    }

    public void setCooperativeMetropole(String cooperativeMetropole) {
        this.cooperativeMetropole = cooperativeMetropole;
    }

    public String getCooperativeCommune() {
        return this.cooperativeCommune;
    }

    public Cooperative cooperativeCommune(String cooperativeCommune) {
        this.setCooperativeCommune(cooperativeCommune);
        return this;
    }

    public void setCooperativeCommune(String cooperativeCommune) {
        this.cooperativeCommune = cooperativeCommune;
    }

    public Set<Livreur> getLivreurs() {
        return this.livreurs;
    }

    public void setLivreurs(Set<Livreur> livreurs) {
        this.livreurs = livreurs;
    }

    public Cooperative livreurs(Set<Livreur> livreurs) {
        this.setLivreurs(livreurs);
        return this;
    }

    public Cooperative addLivreur(Livreur livreur) {
        this.livreurs.add(livreur);
        livreur.getCooperatives().add(this);
        return this;
    }

    public Cooperative removeLivreur(Livreur livreur) {
        this.livreurs.remove(livreur);
        livreur.getCooperatives().remove(this);
        return this;
    }

    public Set<Restaurateur> getRestaurateurs() {
        return this.restaurateurs;
    }

    public void setRestaurateurs(Set<Restaurateur> restaurateurs) {
        this.restaurateurs = restaurateurs;
    }

    public Cooperative restaurateurs(Set<Restaurateur> restaurateurs) {
        this.setRestaurateurs(restaurateurs);
        return this;
    }

    public Cooperative addRestaurateur(Restaurateur restaurateur) {
        this.restaurateurs.add(restaurateur);
        restaurateur.getCooperatives().add(this);
        return this;
    }

    public Cooperative removeRestaurateur(Restaurateur restaurateur) {
        this.restaurateurs.remove(restaurateur);
        restaurateur.getCooperatives().remove(this);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        this.clients = clients;
    }

    public Cooperative clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Cooperative addClient(Client client) {
        this.clients.add(client);
        client.getCooperatives().add(this);
        return this;
    }

    public Cooperative removeClient(Client client) {
        this.clients.remove(client);
        client.getCooperatives().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", cooperativeId=" + getCooperativeId() +
            ", cooperativeNom='" + getCooperativeNom() + "'" +
            ", cooperativeDirecteur='" + getCooperativeDirecteur() + "'" +
            ", cooperativeVille='" + getCooperativeVille() + "'" +
            ", cooperativeMetropole='" + getCooperativeMetropole() + "'" +
            ", cooperativeCommune='" + getCooperativeCommune() + "'" +
            "}";
    }
}
