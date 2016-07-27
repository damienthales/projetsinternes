package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Formation.
 */
@Entity
@Table(name = "formation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Formation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "formation_nom", nullable = false)
    private String formationNom;

    @NotNull
    @Column(name = "formation_date", nullable = false)
    private ZonedDateTime formationDate;

    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormationNom() {
        return formationNom;
    }

    public void setFormationNom(String formationNom) {
        this.formationNom = formationNom;
    }

    public ZonedDateTime getFormationDate() {
        return formationDate;
    }

    public void setFormationDate(ZonedDateTime formationDate) {
        this.formationDate = formationDate;
    }

	public Long getIdCollaborateur() {
		return idCollaborateur;
	}

	public void setIdCollaborateur(Long idCollaborateur) {
		this.idCollaborateur = idCollaborateur;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Formation formation = (Formation) o;
        if(formation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, formation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Formation{" +
            "id=" + id +
            ", formationNom='" + formationNom + "'" +
            ", formationDate='" + formationDate + "'" +
            '}';
    }
}
