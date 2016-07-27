package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Email;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Email entity.
 */
@SuppressWarnings("unused")
public interface EmailRepository extends JpaRepository<Email,Long> {

}
