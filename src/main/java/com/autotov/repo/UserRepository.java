package com.autotov.repo;


import com.autotov.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAllByTenant(Integer tenant);

    void deleteAllByTenant(Integer tenant);

    Optional<User> findByNumberIdAndTenant(Integer userId, Integer tenant);

    Optional<User> findByUsernameAndTenant(String username, Integer tenant);

    Optional<User> findByName(String name);

    Optional<User> findByUsername(String username);
}
