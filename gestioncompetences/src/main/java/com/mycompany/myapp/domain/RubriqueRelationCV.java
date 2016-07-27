package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RubriqueRelationCV.
 */
@Entity
@Table(name = "rubrique_relation_cv")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RubriqueRelationCV implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Cv cv;

    @ManyToOne
    private Rubrique rubrique;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cv getCv() {
        return cv;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
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
        RubriqueRelationCV rubriqueRelationCV = (RubriqueRelationCV) o;
        if(rubriqueRelationCV.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rubriqueRelationCV.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RubriqueRelationCV{" +
            "id=" + id +
            '}';
    }
}
