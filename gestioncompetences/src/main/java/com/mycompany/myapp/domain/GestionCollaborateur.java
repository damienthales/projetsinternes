package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.mycompany.myapp.domain.Collaborateur;

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
public class GestionCollaborateur implements Serializable {

    @Id
	
	//@ManyToOne
    private Collaborateur collaborateur;

    

    public Collaborateur getCollaborateur() {
        return collaborateur;
    }
}
