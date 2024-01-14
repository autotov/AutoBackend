package com.autotov.controller;

import com.autotov.app.Constants;
import com.autotov.model.Driver;
import com.autotov.model.DriverResp;
import com.autotov.model.User;
import com.autotov.service.DriverService;
import com.autotov.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private TenantService tenantService;

    @GetMapping(value="/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDriverList(@RequestParam Integer company) {
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.ADMIN_ROLE) && !user.getCompanies().contains(company)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.FORBIDDEN);
        }

        Iterable<Driver> drivers = driverService.getDriverList(user.getTenant(), company);
        List<Driver> newDriverList = new ArrayList<>();
        drivers.forEach(driver -> {
            newDriverList.add(driver);
        });

        return new ResponseEntity<>(newDriverList, HttpStatus.OK);
    }

    @PostMapping("/driver/add")
    public ResponseEntity<Object> addDriver(@RequestBody Driver driver) {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        try {
            driverService.addDriver(driver);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("נהג נוסף בהצלחה", HttpStatus.OK);
    }

    @PostMapping("/driver/update")
    public ResponseEntity<Object> addDriverObject(@RequestBody DriverResp driver) {
        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        Driver updateDriver = new Driver(driver);
        updateDriver.setTenant(user.getTenant());
        updateDriver.setCompany(user.getCurrentCompany());

        try {
            driverService.updateDriver(updateDriver);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("נהג עודכן בהצלחה", HttpStatus.OK);
    }

    @DeleteMapping("/deleteAllDrivers")
    public ResponseEntity<Object> deleteAllDrivers(){
        User user = tenantService.getLoginUser();
        if(!user.getRole().equals(Constants.SUPER_ADMIN)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        driverService.deleteAllDrivers();
        return new ResponseEntity<>("מחיקה הסתיימה בהצלחה", HttpStatus.OK);
    }

    @DeleteMapping("/driver/{driverId}/delete")
    public ResponseEntity<Object> deleteDriver(@PathVariable("driverId") String driverId) {

        User user = tenantService.getLoginUser();
        if(user.getRole().equals(Constants.VIEWER_ROLE)) {
            return new ResponseEntity<>("אין הרשאה לבקשה זו.", HttpStatus.UNAUTHORIZED);
        }

        driverService.deleteDriver(driverId, user.getTenant(), user.getCurrentCompany());

        return new ResponseEntity<>("נהג נמחק בהצלחה", HttpStatus.OK);
    }
}
