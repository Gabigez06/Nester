package es.uclm.Nester.service;

import java.util.List;

import org.springframework.stereotype.Service;

import es.uclm.Nester.domain.Property;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.repository.PropertyRepository;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property publish(Property property) {
        if (property == null) {
            throw new IllegalArgumentException("El inmueble es obligatorio.");
        }

        if (property.getOwner() == null
                || property.getOwner().getRole() != UserRole.PROPIETARIO) {
            throw new IllegalArgumentException(
                    "Solo un propietario puede publicar inmuebles.");
        }

        return propertyRepository.save(property);
    }

    public List<Property> obtenerTodos() {
        return propertyRepository.findAll();
    }
}