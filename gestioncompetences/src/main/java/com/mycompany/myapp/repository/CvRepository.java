package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cv;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cv entity.
 */
@SuppressWarnings("unused")
public interface CvRepository extends JpaRepository<Cv,Long> {

}
