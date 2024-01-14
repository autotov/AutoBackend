package com.autotov.repo;

import com.autotov.model.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends CrudRepository<Driver, String> {
    Iterable<Driver> findByTenantAndCompany(String tenant, String company);

    void deleteByDriverId(String driverId);

    Iterable<Driver> findAllByTenantAndCompany(Integer tenant, Integer company);

    void deleteByDriverIdAndTenantAndCompany(String driverId, Integer tenant, Integer company);
}