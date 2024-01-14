package com.autotov.service;

import com.autotov.model.Driver;

public interface DriverService {

    public Iterable<Driver> getDriverList(Integer tenant, Integer company);

    void addDriver(Driver driver) throws Exception;

    void updateDriver(Driver driver) throws Exception;

    public void deleteDriver(String driverId, Integer tenant, Integer company);

    void deleteAllDrivers();

}