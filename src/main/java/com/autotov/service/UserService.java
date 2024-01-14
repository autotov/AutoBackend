package com.autotov.service;

import com.autotov.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUser(Integer userId, Integer tenant);

    Optional<User> getUser(String username, Integer tenant);

    Optional<User> getUser(String username);

    void addUser(User user) throws Exception;

    void updateUser(User user, boolean setPassword) throws Exception;

    void deleteUser(Integer userId, Integer tenant) throws Exception;

    Iterable<User> getUsersList(Integer tenant);

    void deleteAllUsers(Integer tenant);
}
