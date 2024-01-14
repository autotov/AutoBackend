package com.autotov.service;

import com.autotov.model.User;
import com.autotov.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUser(Integer userId, Integer tenant) {
        return userRepository.findByNumberIdAndTenant(userId, tenant);
    }

    @Override
    public Optional<User> getUser(String userName, Integer tenant) {
        return userRepository.findByUsernameAndTenant(userName, tenant);
    }

    @Override
    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addUser(User user) throws Exception {
        if(getUser(user.getUsername()).isPresent()) {
            throw new Exception("משתמש קיים");
        }

        user.setNumberId(getAvailableNumberId(user));

        userRepository.save(user);
    }

    private Integer getAvailableNumberId(User user) {
        List<User> userList = userRepository.findAllByTenant(user.getTenant());
        int max = 1;
        for (User userItem: userList) {
            int tenantNum = userItem.getNumberId();
            if(tenantNum >= max) {
                max = tenantNum + 1;
            }
        }

        return max;
    }

    @Override
    public void updateUser(User user, boolean setPassword) throws Exception {
        Optional<User> existsUser = getUser(user.getUsername(), user.getTenant());
        if(!existsUser.isPresent()) {
            throw new Exception("משתמש לא קיים");
        }

        user.setNumberId(existsUser.get().getNumberId());
        if(setPassword) {
            user.setPassword(existsUser.get().getPassword());
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer userId, Integer tenant) throws Exception {
        Optional<User> existsUser = getUser(userId, tenant);
        if(!existsUser.isPresent()) {
            throw new Exception("משתמש לא קיים");
        }

        userRepository.delete(existsUser.get());
    }

    @Override
    public Iterable<User> getUsersList(Integer tenant) {
        return userRepository.findAllByTenant(tenant);
    }

    @Override
    public void deleteAllUsers(Integer tenant) {
        userRepository.deleteAllByTenant(tenant);
    }

}
