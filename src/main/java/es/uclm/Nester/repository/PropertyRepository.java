package es.uclm.Nester.repository;

import es.uclm.Nester.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long>{
    
}
