package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Classification.
 */
@Entity
@Table(name = "classification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Classification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "classification_nom", nullable = false)
    private String classificationNom;

    @NotNull
    @Column(name = "classification_date_debut", nullable = false)
    private ZonedDateTime classificationDateDebut;

    @Column(name = "classification_date_fin")
    private ZonedDateTime classificationDateFin;

    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassificationNom() {
        return classificationNom;
    }

    public void setClassificationNom(String classificationNom) {
        this.classificationNom = classificationNom;
    }

    public ZonedDateTime getClassificationDateDebut() {
        return classificationDateDebut;
    }

    public void setClassificationDateDebut(ZonedDateTime classificationDateDebut) {
        this.classificationDateDebut = classificationDateDebut;
    }

    public ZonedDateTime getClassificationDateFin() {
        return classificationDateFin;
    }

    public void setClassificationDateFin(ZonedDateTime classificationDateFin) {
        this.classificationDateFin = classificationDateFin;
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
        Classification classification = (Classification) o;
        if(classification.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, classification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Classification{" +
            "id=" + id +
            ", classificationNom='" + classificationNom + "'" +
            ", classificationDateDebut='" + classificationDateDebut + "'" +
            ", classificationDateFin='" + classificationDateFin + "'" +
            '}';
    }
}
