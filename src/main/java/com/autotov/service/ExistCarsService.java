package com.autotov.service;

import com.autotov.model.ExistCar;

import java.util.Optional;

public interface ExistCarsService {

    //public Iterable<ExistCar> getExistCars();

    Optional<ExistCar>  getCar(Long existCar) throws Exception;

}
