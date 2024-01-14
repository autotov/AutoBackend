package com.autotov.repo;

import com.autotov.model.Car;
import com.autotov.model.CarDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarDocsRepository extends CrudRepository<CarDocument, String> {

    Iterable<CarDocument> findByTenantAndCompanyAndCarLicense(Integer tenant, Integer company, String carLicense);

    void deleteByDocId(String docId);

}
