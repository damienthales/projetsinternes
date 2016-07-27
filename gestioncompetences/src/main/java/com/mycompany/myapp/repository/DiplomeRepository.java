package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Diplome;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Diplome entity.
 */
@SuppressWarnings("unused")
public interface DiplomeRepository extends JpaRepository<Diplome,Long> {

}
