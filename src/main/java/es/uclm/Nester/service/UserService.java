package es.uclm.Nester.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(
            String name,
            String surname,
            String email,
            String phone,
            String password,
            UserRole role) {

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Ese email ya está registrado");
        }

        User user = new User(
                name,
                surname,
                normalizedEmail,
                phone,
                password,
                role
        );

        return userRepository.save(user);
    }
}