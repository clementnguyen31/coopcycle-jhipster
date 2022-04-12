package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Livreur.
 */
@Entity
@Table(name = "livreur")
public class Livreur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "livreur_id")
    private Long livreurId;

    @Column(name = "livreur_nom")
    private String livreurNom;

    @Column(name = "livreur_prenom")
    private String livreurPrenom;

    @Column(name = "livreur_email")
    private String livreurEmail;

    @Column(name = "livreur_adresse")
    private String livreurAdresse;

    @ManyToMany(mappedBy = "livreurs")
    @JsonIgnoreProperties(value = { "livreurs", "restaurateurs", "clients" }, allowSetters = true)
    private Set<Cooperative> cooperatives = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livreur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLivreurId() {
        return this.livreurId;
    }

    public Livreur livreurId(Long livreurId) {
        this.setLivreurId(livreurId);
        return this;
    }

    public void setLivreurId(Long livreurId) {
        this.livreurId = livreurId;
    }

    public String getLivreurNom() {
        return this.livreurNom;
    }

    public Livreur livreurNom(String livreurNom) {
        this.setLivreurNom(livreurNom);
        return this;
    }

    public void setLivreurNom(String livreurNom) {
        this.livreurNom = livreurNom;
    }

    public String getLivreurPrenom() {
        return this.livreurPrenom;
    }

    public Livreur livreurPrenom(String livreurPrenom) {
        this.setLivreurPrenom(livreurPrenom);
        return this;
    }

    public void setLivreurPrenom(String livreurPrenom) {
        this.livreurPrenom = livreurPrenom;
    }

    public String getLivreurEmail() {
        return this.livreurEmail;
    }

    public Livreur livreurEmail(String livreurEmail) {
        this.setLivreurEmail(livreurEmail);
        return this;
    }

    public void setLivreurEmail(String livreurEmail) {
        this.livreurEmail = livreurEmail;
    }

    public String getLivreurAdresse() {
        return this.livreurAdresse;
    }

    public Livreur livreurAdresse(String livreurAdresse) {
        this.setLivreurAdresse(livreurAdresse);
        return this;
    }

    public void setLivreurAdresse(String livreurAdresse) {
        this.livreurAdresse = livreurAdresse;
    }

    public Set<Cooperative> getCooperatives() {
        return this.cooperatives;
    }

    public void setCooperatives(Set<Cooperative> cooperatives) {
        if (this.cooperatives != null) {
            this.cooperatives.forEach(i -> i.removeLivreur(this));
        }
        if (cooperatives != null) {
            cooperatives.forEach(i -> i.addLivreur(this));
        }
        this.cooperatives = cooperatives;
    }

    public Livreur cooperatives(Set<Cooperative> cooperatives) {
        this.setCooperatives(cooperatives);
        return this;
    }

    public Livreur addCooperative(Cooperative cooperative) {
        this.cooperatives.add(cooperative);
        cooperative.getLivreurs().add(this);
        return this;
    }

    public Livreur removeCooperative(Cooperative cooperative) {
        this.cooperatives.remove(cooperative);
        cooperative.getLivreurs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livreur)) {
            return false;
        }
        return id != null && id.equals(((Livreur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livreur{" +
            "id=" + getId() +
            ", livreurId=" + getLivreurId() +
            ", livreurNom='" + getLivreurNom() + "'" +
            ", livreurPrenom='" + getLivreurPrenom() + "'" +
            ", livreurEmail='" + getLivreurEmail() + "'" +
            ", livreurAdresse='" + getLivreurAdresse() + "'" +
            "}";
    }
}
