package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Rubrique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Rubrique entity.
 */
@SuppressWarnings("unused")
public interface RubriqueRepository extends JpaRepository<Rubrique,Long> {

}
