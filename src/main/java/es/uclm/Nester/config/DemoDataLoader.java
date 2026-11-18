package es.uclm.Nester.config;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.uclm.Nester.domain.Property;
import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.repository.PropertyRepository;
import es.uclm.Nester.repository.UserRepository;

@Component
@Profile("dev")
public class DemoDataLoader implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DemoDataLoader(
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (propertyRepository.count() > 0) {
            return;
        }

        User owner = userRepository.findByEmail("demo@nester.local")
                .orElseGet(() -> userRepository.save(new User(
                        "Propietario",
                        "Demo",
                        "demo@nester.local",
                        "600123456",
                        passwordEncoder.encode("Demo12345"),
                        UserRole.PROPIETARIO)));

        Property property = new Property(
                "Apartamento luminoso en el centro",
                "España",
                "Toledo",
                "45001",
                "Calle Mayor, 10",
                Property.PropertyType.PISO,
                2,
                3,
                1,
                4,
                Set.of(Property.Complement.WIFI, Property.Complement.LAVADORA),
                "Apartamento de demostración, cómodo y bien situado.",
                new BigDecimal("85.00"),
                owner);

        propertyRepository.save(property);
    }
}