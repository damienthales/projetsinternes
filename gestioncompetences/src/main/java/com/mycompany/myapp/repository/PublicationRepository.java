package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Publication;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Publication entity.
 */
@SuppressWarnings("unused")
public interface PublicationRepository extends JpaRepository<Publication,Long> {

}
