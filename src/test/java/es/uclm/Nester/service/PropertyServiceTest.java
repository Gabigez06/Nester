package es.uclm.Nester.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

import es.uclm.Nester.repository.PropertyRepository;

class PropertyServiceTest {

    @Test
    void shouldRejectNullProperty() {
        PropertyRepository repository = mock(PropertyRepository.class);
        PropertyService service = new PropertyService(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.publish(null));

        verifyNoInteractions(repository);
    }
}
