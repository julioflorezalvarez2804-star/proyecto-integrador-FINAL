package co.edu.cesde.ga.application.service;

import co.edu.cesde.ga.application.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(String username, String email, String rawPassword);
    UserDTO findById(Long id);
    List<UserDTO> findAll();
    UserDTO update(Long id, String username, String email, String status);
    void delete(Long id);
}
