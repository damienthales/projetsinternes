package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.NiveauCompetence;

/**
 * A DonneesRubrique.
 */
@Entity
@Table(name = "donnees_rubrique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DonneesRubrique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "donnees_rubrique_titre", nullable = false)
    private String donneesRubriqueTitre;

    @NotNull
    @Column(name = "donnees_rubrique_ordre", nullable = false)
    private Integer donneesRubriqueOrdre;

    @NotNull
    @Column(name = "donnees_rubrique_date_debut", nullable = false)
    private ZonedDateTime donneesRubriqueDateDebut;

    @Column(name = "donnees_rubrique_date_fin")
    private ZonedDateTime donneesRubriqueDateFin;

    @Column(name = "donnees_rubrique_poste")
    private String donneesRubriquePoste;

    @Column(name = "donnees_rubrique_client")
    private String donneesRubriqueClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "donnees_rubrique_niveaucompetence")
    private NiveauCompetence donneesRubriqueNiveaucompetence;

    @NotNull
    @Size(max = 2056)
    @Column(name = "donnees_rubrique_description", length = 2056, nullable = false)
    private String donneesRubriqueDescription;

    @NotNull
    @Column(name = "cv_id", nullable = false)
    private Long idCv;

    @ManyToOne
    private Rubrique rubrique;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDonneesRubriqueTitre() {
        return donneesRubriqueTitre;
    }

    public void setDonneesRubriqueTitre(String donneesRubriqueTitre) {
        this.donneesRubriqueTitre = donneesRubriqueTitre;
    }

    public Integer getDonneesRubriqueOrdre() {
        return donneesRubriqueOrdre;
    }

    public void setDonneesRubriqueOrdre(Integer donneesRubriqueOrdre) {
        this.donneesRubriqueOrdre = donneesRubriqueOrdre;
    }

    public ZonedDateTime getDonneesRubriqueDateDebut() {
        return donneesRubriqueDateDebut;
    }

    public void setDonneesRubriqueDateDebut(ZonedDateTime donneesRubriqueDateDebut) {
        this.donneesRubriqueDateDebut = donneesRubriqueDateDebut;
    }

    public ZonedDateTime getDonneesRubriqueDateFin() {
        return donneesRubriqueDateFin;
    }

    public void setDonneesRubriqueDateFin(ZonedDateTime donneesRubriqueDateFin) {
        this.donneesRubriqueDateFin = donneesRubriqueDateFin;
    }

    public String getDonneesRubriquePoste() {
        return donneesRubriquePoste;
    }

    public void setDonneesRubriquePoste(String donneesRubriquePoste) {
        this.donneesRubriquePoste = donneesRubriquePoste;
    }

    public String getDonneesRubriqueClient() {
        return donneesRubriqueClient;
    }

    public void setDonneesRubriqueClient(String donneesRubriqueClient) {
        this.donneesRubriqueClient = donneesRubriqueClient;
    }

    public NiveauCompetence getDonneesRubriqueNiveaucompetence() {
        return donneesRubriqueNiveaucompetence;
    }

    public void setDonneesRubriqueNiveaucompetence(NiveauCompetence donneesRubriqueNiveaucompetence) {
        this.donneesRubriqueNiveaucompetence = donneesRubriqueNiveaucompetence;
    }

    public String getDonneesRubriqueDescription() {
        return donneesRubriqueDescription;
    }

    public void setDonneesRubriqueDescription(String donneesRubriqueDescription) {
        this.donneesRubriqueDescription = donneesRubriqueDescription;
    }

    public Rubrique getRubrique() {
        return rubrique;
    }

    public void setRubrique(Rubrique rubrique) {
        this.rubrique = rubrique;
    }

	public Long getIdCv() {
		return idCv;
	}

	public void setIdCv(Long idCv) {
		this.idCv = idCv;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DonneesRubrique donneesRubrique = (DonneesRubrique) o;
        if(donneesRubrique.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, donneesRubrique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DonneesRubrique{" +
            "id=" + id +
            ", donneesRubriqueTitre='" + donneesRubriqueTitre + "'" +
            ", donneesRubriqueOrdre='" + donneesRubriqueOrdre + "'" +
            ", donneesRubriqueDateDebut='" + donneesRubriqueDateDebut + "'" +
            ", donneesRubriqueDateFin='" + donneesRubriqueDateFin + "'" +
            ", donneesRubriquePoste='" + donneesRubriquePoste + "'" +
            ", donneesRubriqueClient='" + donneesRubriqueClient + "'" +
            ", donneesRubriqueNiveaucompetence='" + donneesRubriqueNiveaucompetence + "'" +
            ", donneesRubriqueDescription='" + donneesRubriqueDescription + "'" +
            '}';
    }
}
