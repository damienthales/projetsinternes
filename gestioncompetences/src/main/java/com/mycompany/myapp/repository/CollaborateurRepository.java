package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Collaborateur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Collaborateur entity.
 */
@SuppressWarnings("unused")
public interface CollaborateurRepository extends JpaRepository<Collaborateur,Long> {

}
