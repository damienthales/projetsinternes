package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.NiveauCompetence;

/**
 * A Competence.
 */
@Entity
@Table(name = "competence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Competence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "competence_libelle")
    private String competenceLibelle;

    @Column(name = "competence_anneesexperiences")
    private Long competenceAnneesexperiences;

    @Column(name = "competence_client")
    private String competenceClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "competence_niveaucompetence")
    private NiveauCompetence competenceNiveauCompetence;

    @ManyToOne
    private Collaborateur collaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompetenceLibelle() {
        return competenceLibelle;
    }

    public void setCompetenceLibelle(String competenceLibelle) {
        this.competenceLibelle = competenceLibelle;
    }

    public Long getCompetenceAnneesexperiences() {
        return competenceAnneesexperiences;
    }

    public void setCompetenceAnneesexperiences(Long competenceAnneesexperiences) {
        this.competenceAnneesexperiences = competenceAnneesexperiences;
    }

    public String getCompetenceClient() {
        return competenceClient;
    }

    public void setCompetenceClient(String competenceClient) {
        this.competenceClient = competenceClient;
    }

    public NiveauCompetence getCompetenceNiveauCompetence() {
        return competenceNiveauCompetence;
    }

    public void setCompetenceNiveauCompetence(NiveauCompetence competenceNiveauCompetence) {
        this.competenceNiveauCompetence = competenceNiveauCompetence;
    }

    public Collaborateur getCollaborateur() {
        return collaborateur;
    }

    public void setCollaborateur(Collaborateur collaborateur) {
        this.collaborateur = collaborateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Competence competence = (Competence) o;
        if(competence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, competence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Competence{" +
            "id=" + id +
            ", competenceLibelle='" + competenceLibelle + "'" +
            ", competenceAnneesexperiences='" + competenceAnneesexperiences + "'" +
            ", competenceClient='" + competenceClient + "'" +
            ", competenceNiveauCompetence='" + competenceNiveauCompetence + "'" +
            '}';
    }
}
