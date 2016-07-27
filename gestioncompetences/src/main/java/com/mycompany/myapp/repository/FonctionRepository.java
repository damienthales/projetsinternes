package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Fonction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fonction entity.
 */
@SuppressWarnings("unused")
public interface FonctionRepository extends JpaRepository<Fonction,Long> {

}
