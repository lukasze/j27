package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Enginier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Enginier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnginierRepository extends JpaRepository<Enginier, Long> {}
