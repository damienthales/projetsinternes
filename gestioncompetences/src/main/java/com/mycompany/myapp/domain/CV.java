package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CV.
 */
@Entity
@Table(name = "cv")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CV implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_cv")
    private ZonedDateTime dateCv;

    @ManyToOne
    private Collaborateur collaborateur;

    @ManyToOne
    private Rubrique rubrique;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateCv() {
        return dateCv;
    }

    public void setDateCv(ZonedDateTime dateCv) {
        this.dateCv = dateCv;
    }

    public Collaborateur getCollaborateur() {
        return collaborateur;
    }

    public void setCollaborateur(Collaborateur collaborateur) {
        this.collaborateur = collaborateur;
    }

    public Rubrique getRubrique() {
        return rubrique;
    }

    public void setRubrique(Rubrique rubrique) {
        this.rubrique = rubrique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CV cV = (CV) o;
        if(cV.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cV.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CV{" +
            "id=" + id +
            ", dateCv='" + dateCv + "'" +
            '}';
    }
}
