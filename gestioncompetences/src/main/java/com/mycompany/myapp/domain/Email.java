package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Email.
 */
@Entity
@Table(name = "email")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "email_libelle")
    private String emailLibelle;
    
    @Column(name = "email_is_principale")
    private Boolean emailPrincipale;

    @NotNull
    @Column(name = "collaborateur_id", nullable = false)
    private Long idCollaborateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailLibelle() {
        return emailLibelle;
    }

    public void setEmailLibelle(String emailLibelle) {
        this.emailLibelle = emailLibelle;
    }

	public Boolean getEmailPrincipale() {
		return emailPrincipale;
	}

	public void setEmailPrincipale(Boolean emailPrincipale) {
		this.emailPrincipale = emailPrincipale;
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
        Email email = (Email) o;
        if(email.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, email.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Email{" +
            "id=" + id +
            ", emailLibelle='" + emailLibelle + "'" +
            '}';
    }
}
