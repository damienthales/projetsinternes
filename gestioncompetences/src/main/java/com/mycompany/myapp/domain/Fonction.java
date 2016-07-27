package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Fonction.
 */
@Entity
@Table(name = "fonction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fonction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "fonction_nom", nullable = false)
    private String fonctionNom;

    @NotNull
    @Column(name = "fonction_date_debut", nullable = false)
    private ZonedDateTime fonctionDateDebut;

    @Column(name = "fonction_date_fin")
    private ZonedDateTime fonctionDateFin;

    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFonctionNom() {
        return fonctionNom;
    }

    public void setFonctionNom(String fonctionNom) {
        this.fonctionNom = fonctionNom;
    }

    public ZonedDateTime getFonctionDateDebut() {
        return fonctionDateDebut;
    }

    public void setFonctionDateDebut(ZonedDateTime fonctionDateDebut) {
        this.fonctionDateDebut = fonctionDateDebut;
    }

    public ZonedDateTime getFonctionDateFin() {
        return fonctionDateFin;
    }

    public void setFonctionDateFin(ZonedDateTime fonctionDateFin) {
        this.fonctionDateFin = fonctionDateFin;
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
        Fonction fonction = (Fonction) o;
        if(fonction.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fonction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Fonction{" +
            "id=" + id +
            ", fonctionNom='" + fonctionNom + "'" +
            ", fonctionDateDebut='" + fonctionDateDebut + "'" +
            ", fonctionDateFin='" + fonctionDateFin + "'" +
            '}';
    }
}
