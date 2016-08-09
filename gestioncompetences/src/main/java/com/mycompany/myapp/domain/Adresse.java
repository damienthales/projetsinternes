package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Adresse.
 */
@Entity
@Table(name = "adresse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Adresse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "adresse_numero", nullable = false)
    private String adresseNumero;

    @NotNull
    @Column(name = "adresse_voie", nullable = false)
    private String adresseVoie;

    @Column(name = "adresse_voie_2")
    private String adresseVoie2;

    @NotNull
    @Column(name = "adresse_code_postal", nullable = false)
    private String adresseCodePostal;

    @NotNull
    @Column(name = "adresse_ville", nullable = false)
    private String adresseVille;

    @Column(name = "adresse_pays")
    private String adressePays;
    
    @Column(name = "adresse_is_principale")
    private Boolean adressePrincipale;
    
    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseNumero() {
        return adresseNumero;
    }

    public void setAdresseNumero(String adresseNumero) {
        this.adresseNumero = adresseNumero;
    }

    public String getAdresseVoie() {
        return adresseVoie;
    }

    public void setAdresseVoie(String adresseVoie) {
        this.adresseVoie = adresseVoie;
    }

    public String getAdresseVoie2() {
        return adresseVoie2;
    }

    public void setAdresseVoie2(String adresseVoie2) {
        this.adresseVoie2 = adresseVoie2;
    }

    public String getAdresseCodePostal() {
        return adresseCodePostal;
    }

    public void setAdresseCodePostal(String adresseCodePostal) {
        this.adresseCodePostal = adresseCodePostal;
    }

    public String getAdresseVille() {
        return adresseVille;
    }

    public void setAdresseVille(String adresseVille) {
        this.adresseVille = adresseVille;
    }

    public String getAdressePays() {
        return adressePays;
    }

    public void setAdressePays(String adressePays) {
        this.adressePays = adressePays;
    }

	public Boolean getAdressePrincipale() {
		return adressePrincipale;
	}

	public void setAdressePrincipale(Boolean adressePrincipale) {
		this.adressePrincipale = adressePrincipale;
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
        Adresse adresse = (Adresse) o;
        if(adresse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adresse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Adresse{" +
            "id=" + id +
            ", adresseNumero='" + adresseNumero + "'" +
            ", adresseVoie='" + adresseVoie + "'" +
            ", adresseVoie2='" + adresseVoie2 + "'" +
            ", adresseCodePostal='" + adresseCodePostal + "'" +
            ", adresseVille='" + adresseVille + "'" +
            ", adressePays='" + adressePays + "'" +
            '}';
    }
}
