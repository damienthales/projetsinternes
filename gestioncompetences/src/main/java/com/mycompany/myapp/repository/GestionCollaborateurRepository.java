package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GestionCollaborateur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GestionCollaborateur entity.
 */
@SuppressWarnings("unused")
public interface GestionCollaborateurRepository extends JpaRepository<GestionCollaborateur,Long> {

}
