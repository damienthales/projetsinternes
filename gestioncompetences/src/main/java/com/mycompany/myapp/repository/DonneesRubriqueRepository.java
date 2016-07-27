package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DonneesRubrique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DonneesRubrique entity.
 */
@SuppressWarnings("unused")
public interface DonneesRubriqueRepository extends JpaRepository<DonneesRubrique,Long> {

}
