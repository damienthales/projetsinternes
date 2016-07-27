package com.mycompany.myapp.web.rest.dto;

import com.mycompany.myapp.domain.Collaborateur;
import com.mycompany.myapp.domain.Adresse;

public class GestionCollaborateurDTO {
	
	Collaborateur collaborateur;
	
	Adresse adresse;
	
	

	public GestionCollaborateurDTO(Collaborateur collaborateur,
			Adresse adresse) {
		super();
		this.collaborateur = collaborateur;
		this.adresse = adresse;
	}
	

	public Collaborateur getCollaborateur() {
		return collaborateur;
	}

	public void setCollaborateur(Collaborateur collaborateur) {
		this.collaborateur = collaborateur;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
		this.adresse = adresse;
	}
	
	

}
