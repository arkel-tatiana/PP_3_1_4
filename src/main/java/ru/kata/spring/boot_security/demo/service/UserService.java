package ru.kata.spring.boot_security.demo.service;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    void saveUser(User userSave);

    void deleteUser(Long id);

    User findUser(Long id);

    @Transactional(readOnly = true)
    User findUserByName(String nameUser);

    void updateUser(User userUpdate);
}
