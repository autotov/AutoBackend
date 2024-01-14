package com.autotov.repo;

import com.autotov.model.Car;
import com.autotov.model.ExistCar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface ExistCarsRepository {//extends CrudRepository<ExistCar, String> {

   /* Optional<ExistCar> findByMispar_rechev(Long mispar_rechev);

    void deleteByMispar_rechev(Long mispar_rechev);*/
}
