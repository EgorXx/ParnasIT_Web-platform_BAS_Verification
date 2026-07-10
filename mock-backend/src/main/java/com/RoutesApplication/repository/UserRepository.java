package com.RoutesApplication.repository;

import com.RoutesApplication.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    private long sequence = 1;


    public User save(User user) {

        user.setId(sequence++);
        users.add(user);

        return user;
    }


    public Optional<User> findByEmail(String email) {

        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

}