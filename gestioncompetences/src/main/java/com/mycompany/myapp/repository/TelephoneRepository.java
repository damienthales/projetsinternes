package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Telephone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Telephone entity.
 */
@SuppressWarnings("unused")
public interface TelephoneRepository extends JpaRepository<Telephone,Long> {

}
