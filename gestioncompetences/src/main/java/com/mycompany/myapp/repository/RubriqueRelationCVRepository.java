package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RubriqueRelationCV;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RubriqueRelationCV entity.
 */
@SuppressWarnings("unused")
public interface RubriqueRelationCVRepository extends JpaRepository<RubriqueRelationCV,Long> {

}
