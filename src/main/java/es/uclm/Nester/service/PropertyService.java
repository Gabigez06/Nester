package es.uclm.Nester.service;

import es.uclm.Nester.domain.Property;
import es.uclm.Nester.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> obtenerTodos() {
        return propertyRepository.findAll();
    }
}