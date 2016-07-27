package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DonneesCollaborateur.
 */
@Entity
@Table(name = "donnees_collaborateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DonneesCollaborateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description_rubrique_collaborateur")
    private String descriptionRubriqueCollaborateur;

    @OneToOne
    @JoinColumn(unique = true)
    private Rubrique rubrique;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriptionRubriqueCollaborateur() {
        return descriptionRubriqueCollaborateur;
    }

    public void setDescriptionRubriqueCollaborateur(String descriptionRubriqueCollaborateur) {
        this.descriptionRubriqueCollaborateur = descriptionRubriqueCollaborateur;
    }

    public Rubrique getRubrique() {
        return rubrique;
    }

    public void setRubrique(Rubrique rubrique) {
        this.rubrique = rubrique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DonneesCollaborateur donneesCollaborateur = (DonneesCollaborateur) o;
        if(donneesCollaborateur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, donneesCollaborateur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DonneesCollaborateur{" +
            "id=" + id +
            ", descriptionRubriqueCollaborateur='" + descriptionRubriqueCollaborateur + "'" +
            '}';
    }
}
