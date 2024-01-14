package com.autotov.repo;

import com.autotov.model.Alerts;
import org.springframework.data.repository.CrudRepository;

public interface AlertsRepository extends CrudRepository<Alerts, String> {
    Iterable<Alerts> findByTenantAndCompany(String tenant, String company);
}
