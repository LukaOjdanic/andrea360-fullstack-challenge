package com.andrea.fitness.service;

import java.util.List;
import java.util.UUID;

import com.andrea.fitness.model.Location;
import com.andrea.fitness.model.User;

public interface AdminService {
    Location createLocation(String name, String address);
    List<Location> getAllLocations();
    User createEmployee(String firstName, String lastName, String email, String password, UUID locationId);
    List<User> getEmployeesByLocation(UUID locationId);

}
