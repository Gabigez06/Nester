package es.uclm.Nester.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(
            String name,
            String surname,
            String email,
            String phone,
            String password,
            UserRole role) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }

        // Se recortan los espacios exteriores; la contraseña no se modifica.
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres.");
        }

        if (role == null) {
            throw new IllegalArgumentException("Selecciona un tipo de usuario.");
        }

        String normalizedName = name.trim();
        String normalizedSurname = surname.trim();
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        String normalizedPhone = phone.trim();

        // Comprobación básica del formato del email.
        if (!normalizedEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("El formato del email no es válido.");
        }

        // Acepta números con +, espacios, guiones o paréntesis.
        String phoneDigits = normalizedPhone.replaceAll("[\\s()-]", "");
        if (!phoneDigits.matches("\\+?\\d{7,15}")) {
            throw new IllegalArgumentException("El formato del teléfono no es válido.");
        }

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Ese email ya está registrado.");
        }

        User user = new User(
                normalizedName,
                normalizedSurname,
                normalizedEmail,
                normalizedPhone,
                passwordEncoder.encode(password),
                role
        );

        return userRepository.save(user);
    }
        public User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        // Buscamos al usuario por email. Si no existe, lanzamos error genérico por seguridad.
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectos."));

        // Comprobamos la contraseña usando el PasswordEncoder (BCrypt)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Email o contraseña incorrectos.");
        }

        return user;
    }
}