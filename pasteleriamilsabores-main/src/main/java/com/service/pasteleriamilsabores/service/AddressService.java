package com.service.pasteleriamilsabores.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.AddressDto;
import com.service.pasteleriamilsabores.models.Address;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.repository.AddressRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressDto> listByUserRun(String userRun) {
        ensureUserExists(userRun);
        return addressRepository.findByUserRun(userRun).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressDto addAddress(String userRun, AddressDto dto) {
        User user = userRepository.findById(userRun)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userRun));
        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setAddress(dto.getAddress());
        address.setRegion(dto.getRegion());
        address.setComuna(dto.getComuna());
        address.setUser(user);
        Address saved = addressRepository.save(address);
        return toDto(saved);
    }

    private void ensureUserExists(String run) {
        if (!userRepository.existsById(run)) {
            throw new EntityNotFoundException("Usuario no encontrado: " + run);
        }
    }

    private AddressDto toDto(Address entity) {
        AddressDto dto = new AddressDto();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setRegion(entity.getRegion());
        dto.setComuna(entity.getComuna());
        return dto;
    }
}
