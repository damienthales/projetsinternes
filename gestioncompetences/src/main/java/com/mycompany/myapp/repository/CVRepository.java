package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CV;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CV entity.
 */
@SuppressWarnings("unused")
public interface CVRepository extends JpaRepository<CV,Long> {

}
