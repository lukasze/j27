package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UNrepresentative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UNrepresentative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UNrepresentativeRepository extends JpaRepository<UNrepresentative, Long> {}
