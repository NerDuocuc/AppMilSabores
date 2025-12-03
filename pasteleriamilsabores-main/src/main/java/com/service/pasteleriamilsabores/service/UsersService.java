package com.service.pasteleriamilsabores.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.pasteleriamilsabores.dto.RegistrationRequest;
import com.service.pasteleriamilsabores.dto.UserProfileUpdateRequest;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.models.UserType;
import com.service.pasteleriamilsabores.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    // backward-compatible delegate used by existing controllers
    public List<User> getAll() {
        return listarUsuarios();
    }

    public User buscarPorRun(String run) {
        return userRepository.findById(run)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con run: " + run));
    }

    public User registerUser(RegistrationRequest req) {
        if (req == null) throw new IllegalArgumentException("Request cannot be null");
        if (req.getRun() == null) throw new IllegalArgumentException("Run is required");
        if (userRepository.existsById(req.getRun())) throw new IllegalArgumentException("Usuario ya existe con run: " + req.getRun());

        String correo = req.getCorreo();
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("Correo es obligatorio");
        }
        if (userRepository.existsByCorreoIgnoreCase(correo.trim())) {
            throw new IllegalArgumentException("Correo ya registrado: " + correo.trim());
        }

        User u = new User();
        u.setRun(req.getRun());
        u.setNombre(req.getNombre());
        u.setApellidos(req.getApellidos());
        u.setCorreo(correo.trim());
        if (req.getTelefono() != null && !req.getTelefono().isBlank()) {
            u.setTelefono(validateTelefono(req.getTelefono()));
        }
        u.setFechaNacimiento(req.getFechaNacimiento());
        // tipoUsuario may be null
        if (req.getTipoUsuario() != null) {
            u.setTipoUsuario(UserType.fromString(req.getTipoUsuario()));
        }
        if (req.getPassword() == null || req.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        // registration meta
        u.setRegistrationCode(req.getRegistrationCode());
        u.setRegistrationDate(LocalDate.now());

        // business rule: 50% discount for users >= 50 years
        try {
            if (req.getFechaNacimiento() != null) {
                LocalDate dob = LocalDate.parse(req.getFechaNacimiento());
                int age = Period.between(dob, LocalDate.now()).getYears();
                if (age >= 50) {
                    u.setDiscountPercent(50);
                }
                // free cake if Duoc student and registering on birthday
                if (req.getCorreo() != null && req.getCorreo().toLowerCase().contains("duoc.cl")) {
                    LocalDate today = LocalDate.now();
                    if (dob.getMonth() == today.getMonth() && dob.getDayOfMonth() == today.getDayOfMonth()) {
                        u.setFreeCakeEligible(Boolean.TRUE);
                    }
                }
            }
        } catch (DateTimeParseException ex) {
            // ignore DOB parsing issues; no age-based discount
        }

        // registration code benefit
        if (req.getRegistrationCode() != null && "FELICES50".equalsIgnoreCase(req.getRegistrationCode().trim())) {
            u.setLifetimeDiscountPercent(10);
        }

        return userRepository.save(u);
    }

    public User updateUserProfile(String run, UserProfileUpdateRequest update) {
        User existing = userRepository.findById(run)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + run));

        if (update == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        String correo = update.getCorreo();
        if (correo != null && !correo.isBlank()) {
            if (userRepository.existsByCorreoIgnoreCaseAndRunNot(correo.trim(), run)) {
                throw new IllegalArgumentException("Correo ya registrado: " + correo.trim());
            }
            existing.setCorreo(correo.trim());
        }

        if (update.getNombre() != null) {
            existing.setNombre(update.getNombre());
        }
        if (update.getApellidos() != null) {
            existing.setApellidos(update.getApellidos());
        }
        if (update.getFechaNacimiento() != null) {
            existing.setFechaNacimiento(update.getFechaNacimiento());
        }
        if (update.getTelefono() != null) {
            existing.setTelefono(validateTelefono(update.getTelefono()));
        }
        if (update.getTipoUsuario() != null) {
            existing.setTipoUsuario(UserType.fromString(update.getTipoUsuario()));
        }

        return userRepository.save(existing);
    }

    private String validateTelefono(String telefono) {
        String value = telefono != null ? telefono.trim() : null;
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (!value.matches("\\+569\\d{8}")) {
            throw new IllegalArgumentException("Telefono debe ir en formato +569XXXXXXXX");
        }
        return value;
    }
}
