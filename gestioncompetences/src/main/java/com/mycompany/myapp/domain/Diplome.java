package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Diplome.
 */
@Entity
@Table(name = "diplome")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Diplome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "diplome_nom", nullable = false)
    private String diplomeNom;

    @NotNull
    @Column(name = "diplome_date", nullable = false)
    private ZonedDateTime diplomeDate;

    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiplomeNom() {
        return diplomeNom;
    }

    public void setDiplomeNom(String diplomeNom) {
        this.diplomeNom = diplomeNom;
    }

    public ZonedDateTime getDiplomeDate() {
        return diplomeDate;
    }

    public void setDiplomeDate(ZonedDateTime diplomeDate) {
        this.diplomeDate = diplomeDate;
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
        Diplome diplome = (Diplome) o;
        if(diplome.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, diplome.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Diplome{" +
            "id=" + id +
            ", diplomeNom='" + diplomeNom + "'" +
            ", diplomeDate='" + diplomeDate + "'" +
            '}';
    }
}
