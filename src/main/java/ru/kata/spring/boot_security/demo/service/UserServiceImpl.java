package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
@Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
         return userRepository.findAll();
    }

    @Override
    @Transactional()
    public void saveUser(User userSave) {
        userSave.setPassword(passwordEncoder.encode(userSave.getPassword()));
        userRepository.save(userSave);
    }

    @Override
    @Transactional()
    public void deleteUser(Long id) { userRepository.deleteById(id);}

    @Override
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        User userFind = null;
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userFind = optionalUser.get();
        }
        return userFind;
    }
    @Override
    @Transactional(readOnly = true)
    public User findUserByName(String nameUser) {
        User userFindByName = null;
        Optional<User> optionalUser = userRepository.findByUserName(nameUser);
        if (optionalUser.isPresent()) {
            userFindByName = optionalUser.get();
        }
        return userFindByName;
    }
    @Override
    @Transactional()
    public void updateUser(User userUpdate) {
        User userNotUpdate = findUserByName(userUpdate.getUsername());
        if (userUpdate.getPassword().isEmpty()) {
           userUpdate.setPassword(userNotUpdate.getPassword());
       } else if (!userNotUpdate.getPassword().equals(userUpdate.getPassword())) {
           userUpdate.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
       }
       userRepository.save(userUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        if (optionalUser.isEmpty()) {
            System.out.println(username);
            throw new UsernameNotFoundException("Пользователь с таким именем не найден");
        }
//        User userIsFind = optionalUser.get();
//        Hibernate.initialize(userIsFind.getRoles());
         return optionalUser.get();
    }

}
