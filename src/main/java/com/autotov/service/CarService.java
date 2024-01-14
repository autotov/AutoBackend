package com.autotov.service;

import com.autotov.model.Car;
import com.autotov.model.CarDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CarService {

    public Iterable<Car> getCarList();

    void deleteAllCars();

    Iterable<Car> getCarList(Integer tenant, Integer company);

    Optional<Car> getCar(String license, Integer tenant, Integer company) throws Exception;

    void addCar(Car car) throws Exception;

    void updateCar(Car car) throws Exception;

    void deleteCar(String license, Integer tenant, Integer company) throws Exception;

    void storeCarFile(MultipartFile file, String name, String type, String carId) throws Exception;

    Iterable<CarDocument> getCarDocsList(Integer tenant, Integer company, String carLicence);

    Optional<CarDocument> getCarDocument(String docId, String license, Integer tenant, Integer company) throws Exception;

    void deleteCarDoc(Integer tenant, Integer company, String docId) throws Exception;

    CarDocument getCarDocFile(String docId, Integer tenant, Integer company) throws Exception;
}
