package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Sexe;

import com.mycompany.myapp.domain.enumeration.EtatMarital;

import com.mycompany.myapp.domain.enumeration.Langue;

/**
 * A Collaborateur.
 */
@Entity
@Table(name = "collaborateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "collaborateur")
public class Collaborateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "collaborateur_numero_tgi", nullable = false)
    private String collaborateurNumeroTgi;
    
    @NotNull
    @Column(name = "collaborateur_matricule", nullable = false)
    private String collaborateurMatricule;
    
    @NotNull
    @Column(name = "collaborateur_nom", nullable = false)
    private String collaborateurNom;

    @NotNull
    @Column(name = "collaborateur_prenom", nullable = false)
    private String collaborateurPrenom;

    @NotNull
    @Column(name = "collaborateur_date_naissance", nullable = false)
    private ZonedDateTime collaborateurDateNaissance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "collaborateur_sexe", nullable = false)
    private Sexe collaborateurSexe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "collaborateur_etat_marital", nullable = false)
    private EtatMarital collaborateurEtatMarital;

    @Column(name = "collaborateur_nombre_enfant")
    private Integer collaborateurNombreEnfant;

    @NotNull
    @Column(name = "collaborateur_date_arrivee", nullable = false)
    private ZonedDateTime collaborateurDateArrivee;

    @Lob
    @Column(name = "collaborateur_photo")
    private byte[] collaborateurPhotos;

    @Column(name = "collaborateur_photo_content_type")
    private String collaborateurPhotosContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "collaborateur_langue")
    private Langue collaborateurLangue;

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Email> emails = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Adresse> adresses = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Fonction> fonctions = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Classification> classifications = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Formation> formations = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Publication> publications = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Diplome> diplomes = new HashSet<>();

    @OneToMany(mappedBy = "idCollaborateur", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Cv> cvs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getCollaborateurNumeroTgi() {
		return collaborateurNumeroTgi;
	}

	public void setCollaborateurNumeroTgi(String collaborateurNumeroTgi) {
		this.collaborateurNumeroTgi = collaborateurNumeroTgi;
	}

	public String getCollaborateurMatricule() {
		return collaborateurMatricule;
	}

	public void setCollaborateurMatricule(String collaborateurMatricule) {
		this.collaborateurMatricule = collaborateurMatricule;
	}

    public String getCollaborateurNom() {
        return collaborateurNom;
    }

    public void setCollaborateurNom(String collaborateurNom) {
        this.collaborateurNom = collaborateurNom;
    }

    public String getCollaborateurPrenom() {
        return collaborateurPrenom;
    }

    public void setCollaborateurPrenom(String collaborateurPrenom) {
        this.collaborateurPrenom = collaborateurPrenom;
    }

    public ZonedDateTime getCollaborateurDateNaissance() {
        return collaborateurDateNaissance;
    }

    public void setCollaborateurDateNaissance(ZonedDateTime collaborateurDateNaissance) {
        this.collaborateurDateNaissance = collaborateurDateNaissance;
    }

    public Sexe getCollaborateurSexe() {
        return collaborateurSexe;
    }

    public void setCollaborateurSexe(Sexe collaborateurSexe) {
        this.collaborateurSexe = collaborateurSexe;
    }

    public EtatMarital getCollaborateurEtatMarital() {
        return collaborateurEtatMarital;
    }

    public void setCollaborateurEtatMarital(EtatMarital collaborateurEtatMarital) {
        this.collaborateurEtatMarital = collaborateurEtatMarital;
    }

    public Integer getCollaborateurNombreEnfant() {
        return collaborateurNombreEnfant;
    }

    public void setCollaborateurNombreEnfant(Integer collaborateurNombreEnfant) {
        this.collaborateurNombreEnfant = collaborateurNombreEnfant;
    }

    public ZonedDateTime getCollaborateurDateArrivee() {
        return collaborateurDateArrivee;
    }

    public void setCollaborateurDateArrivee(ZonedDateTime collaborateurDateArrivee) {
        this.collaborateurDateArrivee = collaborateurDateArrivee;
    }

    public byte[] getCollaborateurPhotos() {
        return collaborateurPhotos;
    }

    public void setCollaborateurPhotos(byte[] collaborateurPhotos) {
        this.collaborateurPhotos = collaborateurPhotos;
    }

    public String getCollaborateurPhotosContentType() {
        return collaborateurPhotosContentType;
    }

    public void setCollaborateurPhotosContentType(String collaborateurPhotosContentType) {
        this.collaborateurPhotosContentType = collaborateurPhotosContentType;
    }

    public Langue getCollaborateurLangue() {
        return collaborateurLangue;
    }

    public void setCollaborateurLangue(Langue collaborateurLangue) {
        this.collaborateurLangue = collaborateurLangue;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }

    public Set<Adresse> getAdresses() {
        return adresses;
    }

    public void setAdresses(Set<Adresse> adresses) {
        this.adresses = adresses;
    }

    public Set<Fonction> getFonctions() {
        return fonctions;
    }

    public void setFonctions(Set<Fonction> fonctions) {
        this.fonctions = fonctions;
    }

    public Set<Classification> getClassifications() {
        return classifications;
    }

    public void setClassifications(Set<Classification> classifications) {
        this.classifications = classifications;
    }

    public Set<Formation> getFormations() {
        return formations;
    }

    public void setFormations(Set<Formation> formations) {
        this.formations = formations;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Set<Diplome> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(Set<Diplome> diplomes) {
        this.diplomes = diplomes;
    }

    public Set<Cv> getCvs() {
        return cvs;
    }

    public void setCvs(Set<Cv> cvs) {
        this.cvs = cvs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Collaborateur collaborateur = (Collaborateur) o;
        if(collaborateur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, collaborateur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Collaborateur{" +
            "id=" + id +
            ", collaborateurNumeroTgi='" + collaborateurNumeroTgi + "'" +
            ", collaborateurMatricule='" + collaborateurMatricule + "'" +
            ", collaborateurNom='" + collaborateurNom + "'" +
            ", collaborateurPrenom='" + collaborateurPrenom + "'" +
            ", collaborateurDateNaissance='" + collaborateurDateNaissance + "'" +
            ", collaborateurSexe='" + collaborateurSexe + "'" +
            ", collaborateurEtatMarital='" + collaborateurEtatMarital + "'" +
            ", collaborateurNombreEnfant='" + collaborateurNombreEnfant + "'" +
            ", collaborateurDateArrivee='" + collaborateurDateArrivee + "'" +
            ", collaborateurPhotos='" + collaborateurPhotos + "'" +
            ", collaborateurPhotosContentType='" + collaborateurPhotosContentType + "'" +
            ", collaborateurLangue='" + collaborateurLangue + "'" +
            '}';
    }
}
