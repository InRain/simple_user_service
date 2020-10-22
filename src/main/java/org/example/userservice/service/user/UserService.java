package org.example.userservice.service.user;

import org.example.userservice.model.User;

import java.util.List;

public interface UserService {
    User findById(Long id);

    User save(User user);

    void delete(Long id);

    void delete(User user);

    List<User> getAll();

    User findByLogin(String login);
}
