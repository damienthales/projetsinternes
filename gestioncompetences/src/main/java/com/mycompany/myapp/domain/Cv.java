package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Cv.
 */
@Entity
@Table(name = "cv")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cv implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@NotNull
    @Column(name = "cv_date")
    private ZonedDateTime cvDate;

	
    @Column(name = "cv_libelle")
    private String cvLibelle;
    
    @Column(name = "cv_etat")
    private String cvEtat;

    @OneToMany(mappedBy = "idCv", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<DonneesRubrique> donnees = new HashSet<>();

    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCvDate() {
        return cvDate;
    }

    public void setCvDate(ZonedDateTime cvDate) {
        this.cvDate = cvDate;
    }

    public String getCvLibelle() {
        return cvLibelle;
    }

    public void setCvLibelle(String cvLibelle) {
        this.cvLibelle = cvLibelle;
    }

    public Set<DonneesRubrique> getDonnees() {
        return donnees;
    }

    public void setDonnees(Set<DonneesRubrique> donneesRubriques) {
        this.donnees = donneesRubriques;
    }

	public Long getIdCollaborateur() {
		return idCollaborateur;
	}

	public void setIdCollaborateur(Long idCollaborateur) {
		this.idCollaborateur = idCollaborateur;
	}

	public String getCvEtat() {
		return cvEtat;
	}

	public void setCvEtat(String cvEtat) {
		this.cvEtat = cvEtat;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cv cv = (Cv) o;
        if(cv.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cv.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cv{" +
            "id=" + id +
            ", cvLibelle='" + cvLibelle + "'" +
            ", cvDate='" + cvDate + "'" +
            ", cvEtat='" + cvEtat + "'" +
            '}';
    }
}
