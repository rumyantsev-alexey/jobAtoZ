package ru.job4j.shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortener.RandomString;
import ru.job4j.shortener.models.User;
import ru.job4j.shortener.repositories.UsersRepository;

@Service
public class UsersService {
    private static final int PASS_LEN = 6;

    @Autowired
    private UsersRepository users;

    @Autowired
    private PasswordEncoder passenc;

    public User findUserByName(String name) {
        return users.findByName(name);
    }

    @Transactional
    public String createUserByUsername(String username) {
        User user = new User(username);
        String password = RandomString.genString(PASS_LEN);
        user.setPassword(passenc.encode(password));
        users.save(user);
        return  password;
    }

}
