package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.TypeExperience;

/**
 * A Experience.
 */
@Entity
@Table(name = "experience")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Experience implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "experience_nom", nullable = false)
    private String experienceNom;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_typeexperience")
    private TypeExperience experienceTypeExperience;

    @ManyToOne
    private Collaborateur collaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExperienceNom() {
        return experienceNom;
    }

    public void setExperienceNom(String experienceNom) {
        this.experienceNom = experienceNom;
    }

    public TypeExperience getExperienceTypeExperience() {
        return experienceTypeExperience;
    }

    public void setExperienceTypeExperience(TypeExperience experienceTypeExperience) {
        this.experienceTypeExperience = experienceTypeExperience;
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
        Experience experience = (Experience) o;
        if(experience.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, experience.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Experience{" +
            "id=" + id +
            ", experienceNom='" + experienceNom + "'" +
            ", experienceTypeExperience='" + experienceTypeExperience + "'" +
            '}';
    }
}
