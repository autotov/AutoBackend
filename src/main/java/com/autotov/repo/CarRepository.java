package com.autotov.repo;

import com.autotov.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends CrudRepository<Car, String> {
    Iterable<Car> findByTenantAndCompany(Integer tenant, Integer company);

    Optional<Car> findByLicense(String license);

    void deleteByLicense(String license);
}
