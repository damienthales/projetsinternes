package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Experience;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Experience entity.
 */
@SuppressWarnings("unused")
public interface ExperienceRepository extends JpaRepository<Experience,Long> {

}
