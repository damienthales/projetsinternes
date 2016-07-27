package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Competence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Competence entity.
 */
@SuppressWarnings("unused")
public interface CompetenceRepository extends JpaRepository<Competence,Long> {

}
