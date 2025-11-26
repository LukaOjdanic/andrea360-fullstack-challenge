package com.andrea.fitness.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.Role;
import com.andrea.fitness.model.User;
import com.andrea.fitness.repository.LocationRepository;
import com.andrea.fitness.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Location createLocation(String name, String address) {
        Location location = new Location();
        location.setName(name);
        location.setAddress(address);
        return locationRepository.save(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    @Transactional
    public User createEmployee(String firstName, String lastName, String email, String password, UUID locationId) {
        Location location = locationRepository.findById(locationId)
            .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        // TODO later hash password
        user.setPassword(password); // AuthService should handle this
        user.setRole(Role.valueOf("EMPLOYEE"));
        user.setLocation(location);
        return userRepository.save(user);
    }

    @Override
    public List<User> getEmployeesByLocation(UUID locationId) {
        return userRepository.findByLocationIdAndRole(locationId, Role.valueOf("EMPLOYEE"));
    }

}
